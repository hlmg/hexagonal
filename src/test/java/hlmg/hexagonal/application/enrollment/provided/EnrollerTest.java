package hlmg.hexagonal.application.enrollment.provided;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.enrollment.Enrollment;
import hlmg.hexagonal.domain.enrollment.EnrollmentStatus;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import hlmg.hexagonal.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class EnrollerTest extends BaseApplicationServiceTest {

    final Enroller enroller;

    @Test
    void enroll() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        Enrollment enrollment = enroller.enroll(new EnrollRequest(member.getId(), course.getId()));

        assertThat(enrollment.getId()).isNotNull();
        assertThat(enrollment.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enrollment.getEnrolledAt()).isNotNull();
    }

    @Test
    void enroll_AlreadyEnrolled_ThrowsException() {
        Enrollment enrollment = prepareEnrollment();

        assertThatThrownBy(() -> enroller.enroll(new EnrollRequest(enrollment.getMember().getId(), enrollment.getCourse().getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void startStudying() {
        Enrollment enrollment = prepareEnrollment();

        enrollment = enroller.startStudying(enrollment.getId());

        assertThat(enrollment.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.STUDYING);
    }

    @Test
    void completeStudying() {
        Enrollment enrollment = prepareEnrollment();
        enrollment = enroller.startStudying(enrollment.getId());

        enrollment = enroller.completeStudying(enrollment.getId());

        assertThat(enrollment.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
    }

}
