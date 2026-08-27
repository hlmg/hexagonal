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
    void create_ValidData_Success() {
        Instructor instructor = InstructorFixture.createActiveInstructor();

        Course course = new Course(instructor, "title", "description");

        assertThat(course.getInstructor()).isEqualTo(instructor);
        assertThat(course.getTitle()).isEqualTo("title");
        assertThat(course.getStatus()).isEqualTo(CourseStatus.DRAFT);
        assertThat(course.getDetail().getDescription()).isEqualTo("description");
        assertThat(course.getDetail().getCreatedAt()).isNotNull();
    }

    @Test
    void create_InstructorNotActive_ThrowsException() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(() -> new Course(instructor, "title", "description"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void submitForReview_DraftCourse_Success() {
        Course course = CourseFixture.createCourse();
        course.submitForReview();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }

    @Test
    void submitForReview_NoDescription_ThrowsException() {
        Instructor instructor = InstructorFixture.createActiveInstructor();
        Course course = new Course(instructor, "title", null);

        assertThatThrownBy(course::submitForReview)
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "DRAFT")
    void submitForReview_NotDraft_ThrowsException(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(course::submitForReview)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void publish_InReviewCourse_Success() {
        Course course = CourseFixture.createCourse();
        course.submitForReview();

        course.publish();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
        assertThat(course.getDetail().getPublishedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "IN_REVIEW")
    void publish_NotInReview_ThrowsException(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(course::publish)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void archive_PublishedCourse_Success() {
        Course course = CourseFixture.createCourse();
        course.submitForReview();
        course.publish();

        course.archive();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
        assertThat(course.getDetail().getArchivedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "PUBLISHED")
    void archive_NotPublished_ThrowsException(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(course::archive)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateInfo_ValidData_Success() {
        Course course = CourseFixture.createCourse();
        course.updateInfo(new CourseUpdateInfo("new lecture", "new description"));

        assertThat(course.getTitle()).isEqualTo("new lecture");
        assertThat(course.getDetail().getDescription()).isEqualTo("new description");
    }

}
