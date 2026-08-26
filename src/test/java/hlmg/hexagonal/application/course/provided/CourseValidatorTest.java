package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.application.course.required.CourseRepository;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.course.CourseFixture;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.exception.ValidationException;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import hlmg.hexagonal.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseValidatorTest extends BaseApplicationServiceTest {

    final CourseValidator courseValidator;
    final CourseRepository courseRepository;

    @Test
    void validateForCreate() {
        Instructor instructor = prepareInstructor();
        courseRepository.save(CourseFixture.createCourse(instructor, "Spring Basic"));
        CourseCreateRequest request = new CourseCreateRequest(instructor.getId(), "New Course", null);

        assertThatNoException()
                .isThrownBy(() -> courseValidator.validateForCreate(instructor, request));
    }

    @Test
    void validateForCreateFailWhenTitleDuplicatedForSameInstructor() {
        Instructor instructor = prepareInstructor();
        Course course = courseRepository.save(CourseFixture.createCourse(instructor, "Spring Basic"));
        CourseCreateRequest request = new CourseCreateRequest(instructor.getId(), course.getTitle(), null);

        assertThatThrownBy(() -> courseValidator.validateForCreate(instructor, request))
                .isInstanceOfSatisfying(ValidationException.class, e ->
                        assertThat(e.getErrors()).hasSize(1).contains("Title already exists: " + course.getTitle()));
    }

    @Test
    void validateForCreateWhenSameTitleUsedByDifferentInstructor() {
        Instructor instructor1 = prepareInstructor();
        Instructor instructor2 = prepareInstructor();
        Course course = courseRepository.save(CourseFixture.createCourse(instructor1, "Spring Basic"));
        CourseCreateRequest request = new CourseCreateRequest(instructor2.getId(), course.getTitle(), null);

        assertThatNoException()
                .isThrownBy(() -> courseValidator.validateForCreate(instructor2, request));
    }

    @Test
    void validateForUpdateWithSameTitle() {
        Instructor instructor = prepareInstructor();
        Course course = courseRepository.save(CourseFixture.createCourse(instructor, "Spring Basic"));
        CourseInfoUpdateRequest updateRequest = CourseFixture.createCourseInfoUpdateRequest(course.getTitle());

        assertThatNoException()
                .isThrownBy(() -> courseValidator.validateForUpdate(course, updateRequest));
    }

    @Test
    void validateForUpdateFailWhenTitleDuplicatedForSameInstructor() {
        Instructor instructor = prepareInstructor();
        Course course1 = courseRepository.save(CourseFixture.createCourse(instructor, "Spring Basic"));
        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor, "Spring Intermediate"));
        CourseInfoUpdateRequest updateRequest = new CourseInfoUpdateRequest(course2.getTitle(), "New Description");

        assertThatThrownBy(() -> courseValidator.validateForUpdate(course1, updateRequest))
                .isInstanceOfSatisfying(ValidationException.class, e ->
                        assertThat(e.getErrors()).hasSize(1).contains("Title already exists: " + course2.getTitle()));
    }

}
