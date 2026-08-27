package hlmg.hexagonal.application.instructor.provided;

import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import hlmg.hexagonal.domain.instructor.InstructorStatus;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import hlmg.hexagonal.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorApplicationTest extends BaseApplicationServiceTest {

    final InstructorApplication instructorApplication;
    final InstructorRepository instructorRepository;

    @Test
    void apply_ValidMember_Success() {
        Member member = prepareMember();

        Instructor instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThat(instructor).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        assertThat(instructorRepository.findById(instructor.getId())).isPresent();
    }

    @Test
    void apply_AlreadyAppliedMember_ThrowsDuplicateInstructorApplicationException() {
        Member member = prepareMember();
        instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThatThrownBy(() -> instructorApplication.apply(InstructorFixture.createApplyRequest(member)))
                .isInstanceOf(DuplicateInstructorApplicationException.class);
    }

    @Test
    void approve_PendingInstructor_Success() {
        Instructor approved = instructorApplication.approve(preparePendingInstructor().getId());

        assertThat(approved.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void reject_PendingInstructor_Success() {
        Instructor approved = instructorApplication.reject(preparePendingInstructor().getId());

        assertThat(approved.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    private Instructor preparePendingInstructor() {
        Member member = prepareMember();

        return instructorApplication.apply(InstructorFixture.createApplyRequest(member));
    }

}
