package hlmg.hexagonal.domain.enrollment;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.course.CourseFixture;
import hlmg.hexagonal.domain.course.CourseStatus;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.domain.member.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnrollmentTest {

    Member activatedMember;
    Course publishedCourse;

    @BeforeEach
    void setUp() {
        activatedMember = MemberFixture.createActiveMember();
        publishedCourse = CourseFixture.createPublishedCourse();
    }

    @Test
    void enroll_activeMemberAndPublishedCourse_success() {
        Enrollment enrollment = Enrollment.enroll(activatedMember, publishedCourse);

        assertThat(enrollment.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enrollment.getEnrolledAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = MemberStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "ACTIVE")
    void enroll_inactiveMember_throwsException(MemberStatus invalidStatus) {
        Member member = MemberFixture.createMember(invalidStatus);

        assertThatThrownBy(() -> Enrollment.enroll(member, publishedCourse))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @EnumSource(value = CourseStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "PUBLISHED")
    void enroll_unpublishedCourse_throwsException(CourseStatus invalidStatus) {
        Course course = CourseFixture.createCourse(invalidStatus);

        assertThatThrownBy(() -> Enrollment.enroll(activatedMember, course))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void startStudying_enrolledStatus_Success() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();

        enrollment.startStudying();

        assertThat(enrollment.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.STUDYING);
    }

    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "ENROLLED")
    void startStudying_NotEnrolled_ThrowsException(EnrollmentStatus invalidStatus) {
        Enrollment enrollment = EnrollmentFixture.createEnrollment(invalidStatus);
        assertThatThrownBy(enrollment::startStudying)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void completeStudying_studyingStatus_Success() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment(EnrollmentStatus.STUDYING);

        enrollment.completeStudying();

        assertThat(enrollment.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(enrollment.getCompletedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "STUDYING")
    void completeStudying_NotStudying_ThrowsException(EnrollmentStatus invalidStatus) {
        Enrollment enrollment = EnrollmentFixture.createEnrollment(invalidStatus);
        assertThatThrownBy(enrollment::completeStudying)
                .isInstanceOf(IllegalStateException.class);
    }

}
