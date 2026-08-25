package hlmg.hexagonal.domain.course;

import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CourseTest {

    @Test
    void create() {
        Instructor instructor = InstructorFixture.createActiveInstructor();

        Course course = new Course(instructor, "title", "description");

        assertThat(course.getInstructor()).isEqualTo(instructor);
        assertThat(course.getTitle()).isEqualTo("title");
        assertThat(course.getStatus()).isEqualTo(CourseStatus.DRAFT);
        assertThat(course.getDetail().getDescription()).isEqualTo("description");
        assertThat(course.getDetail().getCreatedAt()).isNotNull();
    }

    @Test
    void createFailWhenInstructorNotActive() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(() -> new Course(instructor, "title", "description"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void submitForReview() {
        Course course = CourseFixture.createCourse();
        course.submitForReview();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }

    @Test
    void submitForReviewFailWhenNoDescription() {
        Instructor instructor = InstructorFixture.createActiveInstructor();
        Course course = new Course(instructor, "title", null);

        assertThatThrownBy(course::submitForReview)
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "DRAFT")
    void submitForReviewFailWhenNotDraft(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(course::submitForReview)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void publish() {
        Course course = CourseFixture.createCourse();
        course.submitForReview();

        course.publish();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
        assertThat(course.getDetail().getPublishedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "IN_REVIEW")
    void publishFailWhenNotInReview(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(course::publish)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void archive() {
        Course course = CourseFixture.createCourse();
        course.submitForReview();
        course.publish();

        course.archive();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
        assertThat(course.getDetail().getArchivedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "PUBLISHED")
    void archiveFailWhenNotPublished(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(course::archive)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateInfo() {
        Course course = CourseFixture.createCourse();
        course.updateInfo(new CourseUpdateInfo("new lecture", "new description"));

        assertThat(course.getTitle()).isEqualTo("new lecture");
        assertThat(course.getDetail().getDescription()).isEqualTo("new description");
    }

}
