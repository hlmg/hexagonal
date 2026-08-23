package hlmg.hexagonal.domain.instructor;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstructorTest {

    @Test
    void apply() {
        Member member = MemberFixture.createActiveMember();

        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.getMember()).isEqualTo(member);
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void applyFailWhenMemberNotActive() {
        Member pendingMember = MemberFixture.createMember();

        assertThatThrownBy(() -> Instructor.apply(pendingMember))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void approve() {
        Instructor instructor = InstructorFixture.createInstructor();

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        Instructor instructor = InstructorFixture.createInstructor();
        instructor.approve();

        assertThatThrownBy(instructor::approve)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Instructor instructor = InstructorFixture.createInstructor();

        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFail() {
        Instructor instructor = InstructorFixture.createInstructor();
        instructor.reject();

        assertThatThrownBy(instructor::reject)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThat(instructor.isActive()).isFalse();

        instructor.approve();
        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive() {
        Instructor instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(instructor::ensureActive)
                .isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }

}
