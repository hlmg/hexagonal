package hlmg.hexagonal.domain;

import org.jspecify.annotations.NonNull;

public class MemberFixture {

    public static @NonNull MemberRegisterRequest createMemberRegisterRequest() {
        return new MemberRegisterRequest("member@gmail.com", "nickname", "password");
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
