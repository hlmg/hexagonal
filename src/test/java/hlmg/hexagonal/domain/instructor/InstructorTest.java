package hlmg.hexagonal.domain.instructor;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstructorTest {

    @Test
    void apply_ActiveMember_Success() {
        Member member = MemberFixture.createActiveMember();

        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.getMember()).isEqualTo(member);
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void apply_MemberNotActive_ThrowsException() {
        Member pendingMember = MemberFixture.createMember();

        assertThatThrownBy(() -> Instructor.apply(pendingMember))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void approve_PendingInstructor_Success() {
        Instructor instructor = InstructorFixture.createInstructor();

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approve_NotPendingInstructor_ThrowsException() {
        Instructor instructor = InstructorFixture.createInstructor();
        instructor.approve();

        assertThatThrownBy(instructor::approve)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject_PendingInstructor_Success() {
        Instructor instructor = InstructorFixture.createInstructor();

        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void reject_NotPendingInstructor_ThrowsException() {
        Instructor instructor = InstructorFixture.createInstructor();
        instructor.reject();

        assertThatThrownBy(instructor::reject)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive_NotApproved_ReturnsFalse() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThat(instructor.isActive()).isFalse();
    }

    @Test
    void isActive_Approved_ReturnsTrue() {
        Instructor instructor = InstructorFixture.createInstructor();
        instructor.approve();

        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive_NotApproved_ThrowsException() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(instructor::ensureActive)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void ensureActive_Approved_DoesNotThrow() {
        Instructor instructor = InstructorFixture.createInstructor();
        instructor.approve();

        instructor.ensureActive();
    }

}
