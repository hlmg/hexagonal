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
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFail() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.approve();

        assertThatThrownBy(instructor::approve)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFail() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.reject();

        assertThatThrownBy(instructor::reject)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.isActive()).isFalse();

        instructor.approve();
        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThatThrownBy(instructor::ensureActive)
                .isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }

}
