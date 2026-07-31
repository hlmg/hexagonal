package hlmg.hexagonal.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        this.member = Member.create(new MemberCreateRequest("member@gmail.com", "nickname", "password"), passwordEncoder);
    }

    @Test
    void createMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFailWhenNotPending() {
        member.activate();

        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFailWhenPending() {
        assertThatThrownBy(() -> member.deactivate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivateFailWhenAlreadyDeactivated() {
        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> member.deactivate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("password", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("wrongPassword", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("nickname");

        member.changeNickname("newNickname");

        assertThat(member.getNickname()).isEqualTo("newNickname");
    }

    @Test
    void changePassword() {
        member.changePassword("newPassword", passwordEncoder);

        assertThat(member.verifyPassword("newPassword", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

}
