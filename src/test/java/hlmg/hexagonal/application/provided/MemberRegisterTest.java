package hlmg.hexagonal.application.provided;

import hlmg.hexagonal.SimpleTestConfiguration;
import hlmg.hexagonal.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@Import(SimpleTestConfiguration.class)
@SpringBootTest
public record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerFailWhenEmailAlreadyExist() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
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
    void memberRegisterRequestFail(String email, String nickname, String password) {
        MemberRegisterRequest request = new MemberRegisterRequest(email, nickname, password);

        assertThatThrownBy(() -> memberRegister.register(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

}
