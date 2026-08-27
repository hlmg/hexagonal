package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.course.CourseStatus;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import hlmg.hexagonal.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class CoursePublisherTest extends BaseApplicationServiceTest {

    final CoursePublisher coursePublisher;

    Course course;

    @BeforeEach
    void setUp() {
        course = prepareCourse();
    }

    @Test
    void submitForReview_DraftCourse_Success() {
        Course submitted = coursePublisher.submitForReview(course.getId());

        assertThat(submitted.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }

    @Test
    void publish_CourseInReview_Success() {
        coursePublisher.submitForReview(course.getId());

        Course published = coursePublisher.publish(course.getId());

        assertThat(published.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
    }

    @Test
    void archive_PublishedCourse_Success() {
        coursePublisher.submitForReview(course.getId());
        coursePublisher.publish(course.getId());

        Course archived = coursePublisher.archive(course.getId());

        assertThat(archived.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
    }

}
