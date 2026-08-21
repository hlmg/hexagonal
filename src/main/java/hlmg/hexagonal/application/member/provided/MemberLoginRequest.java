package hlmg.hexagonal.application.member.provided;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MemberLoginRequest(
        @Email String email,
        @Size(min = 8, max = 20) String password
) {

}
