package hlmg.hexagonal.application.course.required;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static hlmg.hexagonal.domain.course.CourseFixture.createCourse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class CourseRepositoryTest extends BaseRepositoryTest {

    final CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        member = prepareActiveMember();
        instructor = prepareActiveInstructor(member);
    }

    @Test
    void save_ValidCourse_Success() {
        Course course = createCourse(instructor);

        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();
    }

    @Test
    void save_DuplicateTitleForSameInstructor_Fails() {
        prepareCourse(instructor, "title");

        assertThatThrownBy(() -> courseRepository.save(createCourse(instructor, "title")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByTitleContaining_MatchingTitle_ReturnsMatchedCourses() {
        Course course1 = prepareCourse(instructor, "Spring Basic");
        Course course2 = prepareCourse(instructor, "Spring Intermediate");
        Course course3 = prepareCourse(instructor, "Java Basic");

        assertFindByTitle("Spring", course1, course2);
        assertFindByTitle("Basic", course1, course3);
    }

    @Test
    void findByTitleContaining_NonMatchingTitle_ReturnsEmptyList() {
        prepareCourse(instructor, "Spring Basic");

        assertFindByTitle("No Course");
    }

    void assertFindByTitle(String keyword, Course... expectedCourses) {
        assertThat(courseRepository.findByTitleContaining(keyword))
                .containsExactlyInAnyOrder(expectedCourses);
    }

    @Test
    void findByInstructorId_ExistingInstructor_ReturnsInstructorCourses() {
        Instructor instructor1 = prepareActiveInstructor();
        Instructor instructor2 = prepareActiveInstructor();
        Course course1 = prepareCourse(instructor1, "Spring Basic");
        Course course2 = prepareCourse(instructor1, "Spring Advanced");
        Course course3 = prepareCourse(instructor2, "Java Basic");

        assertFindByInstructorId(instructor1.getId(), course1, course2);
        assertFindByInstructorId(instructor2.getId(), course3);
    }

    @Test
    void findByInstructorId_NonExistingInstructor_ReturnsEmptyList() {
        assertFindByInstructorId(-1L);
    }

    void assertFindByInstructorId(Long instructorId, Course... expectedCourses) {
        assertThat(courseRepository.findByInstructorId(instructorId))
                .containsExactlyInAnyOrder(expectedCourses);
    }

}
