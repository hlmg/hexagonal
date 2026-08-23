package hlmg.hexagonal.application.instructor.provided;

import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import hlmg.hexagonal.domain.instructor.InstructorStatus;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorApplicationTest {

    final InstructorApplication instructorApplication;
    final InstructorRepository instructorRepository;
    final MemberRepository memberRepository;

    @Test
    void apply() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);

        Instructor instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThat(instructor).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        assertThat(instructorRepository.findById(instructor.getId())).isPresent();
    }

    @Test
    void applyFailWhenAlreadyApplied() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);
        instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThatThrownBy(() -> instructorApplication.apply(InstructorFixture.createApplyRequest(member)))
                .isInstanceOf(DuplicateInstructorApplicationException.class);
    }

    @Test
    void approve() {
        Instructor approved = instructorApplication.approve(preparePendingInstructor().getId());

        assertThat(approved.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void reject() {
        Instructor approved = instructorApplication.reject(preparePendingInstructor().getId());

        assertThat(approved.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    private Instructor preparePendingInstructor() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);

        return instructorApplication.apply(InstructorFixture.createApplyRequest(member));
    }

}
