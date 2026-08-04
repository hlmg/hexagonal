package hlmg.hexagonal.adapter.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
record SecurePasswordEncoderTest(SecurePasswordEncoder securePasswordEncoder) {

    @Test
    void setSecurePasswordEncoder() {
        String passwordHash = securePasswordEncoder.encode("password");

        assertThat(securePasswordEncoder.matches("password", passwordHash)).isTrue();
        assertThat(securePasswordEncoder.matches("wrongPassword", passwordHash)).isFalse();
    }

}
