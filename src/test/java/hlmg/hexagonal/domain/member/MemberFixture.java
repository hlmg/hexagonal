package hlmg.hexagonal.domain.member;

import hlmg.hexagonal.application.member.provided.MemberRegisterRequest;
import jakarta.validation.Valid;
import org.instancio.Instancio;
import org.jspecify.annotations.NonNull;
import org.springframework.test.util.ReflectionTestUtils;

import static org.instancio.Select.field;

public class MemberFixture {

    public static @NonNull MemberRegisterRequest createMemberRegisterRequest() {
        String email = Instancio.gen().net().email().get();
        return createMemberRegisterRequest(email);
    }

    public static @Valid MemberRegisterRequest createMemberRegisterRequest(String email) {
        return Instancio.of(MemberRegisterRequest.class)
                .set(field(MemberRegisterRequest::email), email)
                .create();
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

    public static Member createMember() {
        return Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
    }

    public static Member createMember(Long id) {
        Member member = createMember();
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createActiveMember() {
        Member member = createMember();
        member.activate();
        return member;
    }

}
