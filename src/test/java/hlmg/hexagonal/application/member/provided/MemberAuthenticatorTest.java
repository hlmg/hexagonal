package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberAuthenticatorTest {

    final MemberAuthenticator memberAuthenticator;
    final MemberRegister memberRegister;

    @Test
    void login() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        Member member = memberRegister.register(memberRegisterRequest);
        member.activate();

        Member loggedIn = memberAuthenticator.login(new MemberLoginRequest(memberRegisterRequest.email(), memberRegisterRequest.password()));

        assertThat(loggedIn).isEqualTo(member);
    }

    @Test
    void loginFailWhenMemberNotActivated() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(memberRegisterRequest.email(), memberRegisterRequest.password()))
        ).isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailWhenEmailNotExist() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest("notexist@gmail.com", memberRegisterRequest.password()))
        ).isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailWhenInvalidPassword() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(memberRegisterRequest.email(), "wrongpassword"))
        ).isInstanceOf(LoginFailedException.class);
    }

}
