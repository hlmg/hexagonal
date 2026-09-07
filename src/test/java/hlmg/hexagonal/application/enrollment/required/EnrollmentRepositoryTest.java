package hlmg.hexagonal.application.enrollment.required;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.enrollment.Enrollment;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
class EnrollmentRepositoryTest extends BaseRepositoryTest {

    final EnrollmentRepository enrollmentRepository;

    @Test
    void save() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        Enrollment enrollment = Enrollment.enroll(member, course);

        enrollment = enrollmentRepository.save(enrollment);

        assertThat(enrollment.getId()).isNotNull();
    }

    @Test
    void findByMemberId() {
        Member member1 = prepareActiveMember();
        Member member2 = prepareActiveMember();
        Enrollment enrollment1 = prepareEnrollment(member1, preparePublishedCourse());
        Enrollment enrollment2 = prepareEnrollment(member2, preparePublishedCourse());
        Enrollment enrollment3 = prepareEnrollment(member1, preparePublishedCourse());

        List<Enrollment> member1Enrollments = enrollmentRepository.findByMemberId(member1.getId());
        assertThat(member1Enrollments).containsExactlyInAnyOrder(enrollment1, enrollment3);

        List<Enrollment> member2Enrollments = enrollmentRepository.findByMemberId(member2.getId());
        assertThat(member2Enrollments).containsExactlyInAnyOrder(enrollment2);
    }

    @Test
    void findByMemberIdAndCourseId() {
        Member member1 = prepareActiveMember();
        Course course1 = preparePublishedCourse();
        Course course2 = preparePublishedCourse();
        Enrollment enrollment1 = prepareEnrollment(member1, course1);

        Enrollment enrollment = enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course1.getId()).orElseThrow();
        assertThat(enrollment).isEqualTo(enrollment1);

        assertThat(enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course2.getId())).isEmpty();
    }

}
