package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.SimpleTestConfiguration;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@Import(SimpleTestConfiguration.class)
@SpringBootTest
record MemberAuthenticatorTest(MemberAuthenticator memberAuthenticator, MemberRegister memberRegister) {

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
