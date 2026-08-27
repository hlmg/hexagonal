package hlmg.hexagonal.domain.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    void constructor_ValidAddress_Success() {
        String address = "address";

        assertThat(new Profile(address).address()).isEqualTo(address);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890123456",     // exceeds max length
            "ABC123",               // contains uppercase letters
            "user!",                // contains special characters
            "user 123",             // contains whitespace
            "프로필"                  // non-ASCII characters
    })
    void constructor_InvalidAddress_ThrowsException(String address) {
        assertThatThrownBy(() -> new Profile(address))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url_ValidProfile_ReturnsFormattedUrl() {
        Profile profile = new Profile("address");

        assertThat(profile.url()).isEqualTo("@address");
    }

}
