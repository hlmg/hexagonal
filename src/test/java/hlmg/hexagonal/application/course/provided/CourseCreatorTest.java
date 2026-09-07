package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.course.CourseFixture;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import hlmg.hexagonal.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {

    final CourseCreator courseCreator;

    @Test
    void create_ValidRequest_Success() {
        Instructor instructor = prepareActiveInstructor();

        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId()));

        assertThat(course.getId()).isNotNull();
    }

    @Test
    void update_ValidRequest_Success() {
        Instructor instructor = prepareActiveInstructor();
        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId()));
        CourseInfoUpdateRequest courseInfoUpdateRequest = CourseFixture.createCourseInfoUpdateRequest("New title");

        Course updated = courseCreator.update(courseInfoUpdateRequest, course.getId());

        assertThat(updated).isEqualTo(course);
        assertThat(updated.getTitle()).isEqualTo(courseInfoUpdateRequest.title());
        assertThat(updated.getDetail().getDescription()).isEqualTo(courseInfoUpdateRequest.description());
    }

}
