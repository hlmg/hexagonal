package hlmg.hexagonal.domain.member;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;

public class MemberFixture {

    public static @NonNull MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("member@gmail.com");
    }

    public static @Valid MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "nickname", "password");
    }

    public static @NonNull PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

}
