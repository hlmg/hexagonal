package hlmg.hexagonal.domain.member;

import hlmg.hexagonal.application.member.provided.MemberInfoUpdateRequest;
import hlmg.hexagonal.application.member.provided.MemberRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hlmg.hexagonal.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;
    MemberRegisterRequest memberRegisterRequest;

    @BeforeEach
    void setUp() {
        passwordEncoder = createPasswordEncoder();

        memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        member = Member.register(memberRegisterRequest.toInfo(), passwordEncoder);
    }

    @Test
    void register_ValidData_Success() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void activate_PendingMember_Success() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activate_NotPending_ThrowsException() {
        member.activate();

        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate_ActiveMember_Success() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void deactivate_PendingMember_ThrowsException() {
        assertThatThrownBy(() -> member.deactivate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate_AlreadyDeactivated_ThrowsException() {
        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> member.deactivate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword_CorrectPassword_ReturnsTrue() {
        assertThat(member.verifyPassword(memberRegisterRequest.password(), passwordEncoder)).isTrue();
    }

    @Test
    void verifyPassword_WrongPassword_ReturnsFalse() {
        assertThat(member.verifyPassword("wrongPassword", passwordEncoder)).isFalse();
    }

    @Test
    void updateInfo_ActiveMember_Success() {
        member.activate();
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("newNickname", "newprofile", "newIntroduction");

        member.updateInfo(updateRequest.toInfo());

        assertThat(member.getNickname()).isEqualTo(updateRequest.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(updateRequest.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(updateRequest.introduction());
    }

    @Test
    void updateInfo_NotActivated_ThrowsException() {
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("newNickname", "newprofile", "newIntroduction");

        Assertions.assertThatThrownBy(() -> member.updateInfo(updateRequest.toInfo()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void changePassword_ValidData_Success() {
        member.changePassword("newPassword", passwordEncoder);

        assertThat(member.verifyPassword("newPassword", passwordEncoder)).isTrue();
    }

    @Test
    void isActive_Pending_ReturnsFalse() {
        assertThat(member.isActive()).isFalse();
    }

    @Test
    void isActive_Activated_ReturnsTrue() {
        member.activate();

        assertThat(member.isActive()).isTrue();
    }

    @Test
    void isActive_Deactivated_ReturnsFalse() {
        member.activate();
        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

}
