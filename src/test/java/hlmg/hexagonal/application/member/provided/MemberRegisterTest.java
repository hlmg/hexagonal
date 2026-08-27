package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.*;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberRegisterTest {

    final MemberRegister memberRegister;
    final EntityManager entityManager;

    @Test
    void register_ValidRequest_Success() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void register_EmailAlreadyExists_ThrowsDuplicateEmailException() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        assertThatThrownBy(() -> memberRegister.register(memberRegisterRequest))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @ParameterizedTest
    @CsvSource({
            // Invalid Email
            "invalid-email, nickname, password",

            // Invalid Nickname Length
            "member@gmail.com, four, password",
            "member@gmail.com, 123456789012345678901, password123",

            // Invalid Password Length
            "member@gmail.com, nickname, short",
            "member@gmail.com, nickname, 123456789012345678901"
    })
    void register_InvalidRequest_ThrowsConstraintViolationException(String email, String nickname, String password) {
        MemberRegisterRequest request = new MemberRegisterRequest(email, nickname, password);

        assertThatThrownBy(() -> memberRegister.register(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate_PendingMember_Success() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate_ActiveMember_Success() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo_ValidRequest_Success() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("newNickname", "newprofile", "newIntroduction");

        member = memberRegister.updateInfo(member.getId(), updateRequest);

        assertThat(member.getNickname()).isEqualTo(updateRequest.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(updateRequest.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(updateRequest.introduction());
    }

    @Test
    void updateInfo_DuplicatedProfileAddress_ThrowsDuplicateProfileException() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("nickname", "duplicate", "introduction"));
        Member member2 = registerMember("member2@gmail.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("nickname", "duplicate", "introduction")))
                .isInstanceOf(DuplicateProfileException.class);
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

}
