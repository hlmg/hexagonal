package hlmg.hexagonal.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "member@gmail.com",
            "first.last@example.co.kr",
            "user+tag@example.com",
            "user_name%test-1@sub.example.org"
    })
    void create(String address) {
        assertThat(new Email(address).address()).isEqualTo(address);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",                     // empty string
            "invalidEmail",         // missing @
            "@gmail.com",           // missing local part
            "member@",              // missing domain
            "member@gmail",         // missing TLD
            "member@gmail.",        // empty TLD
            "mem ber@gmail.com",    // whitespace
            "member@@gmail.com"     // duplicate @
    })
    void createFail(String address) {
        assertThatThrownBy(() -> new Email(address))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
