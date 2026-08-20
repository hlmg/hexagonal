package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.MemberRegisterInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MemberRegisterRequest(
        @Email String email,
        @Size(min = 5, max = 20) String nickname,
        @Size(min = 8, max = 20) String password
) {

    public MemberRegisterInfo toInfo() {
        return new MemberRegisterInfo(email, nickname, password);
    }

}
