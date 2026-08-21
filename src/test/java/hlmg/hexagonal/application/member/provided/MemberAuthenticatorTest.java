package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.SimpleTestConfiguration;
import hlmg.hexagonal.domain.member.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Import(SimpleTestConfiguration.class)
@SpringBootTest
record MemberAuthenticatorTest(MemberAuthenticator memberAuthenticator, MemberRegister memberRegister) {

    @Test
    void login() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest).activate();

        memberAuthenticator.login(new MemberLoginRequest(memberRegisterRequest.email(), memberRegisterRequest.password()));
    }

    @Test
    void loginFailWhenMemberNotActivated() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        Assertions.assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(memberRegisterRequest.email(), memberRegisterRequest.password()))
        ).isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailWhenEmailNotExist() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        Assertions.assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest("notexist@gmail.com", memberRegisterRequest.password()))
        ).isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailWhenInvalidPassword() {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        Assertions.assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(memberRegisterRequest.email(), "wrongpassword"))
        ).isInstanceOf(LoginFailedException.class);
    }

}
