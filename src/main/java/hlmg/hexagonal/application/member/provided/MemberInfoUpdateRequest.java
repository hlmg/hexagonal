package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.MemberUpdateInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateRequest(
        @Size(min = 5, max = 20) String nickname,
        @NotNull @Size(max = 15) String profileAddress,
        @NotNull String introduction
) {

    public MemberUpdateInfo toInfo() {
        return new MemberUpdateInfo(nickname, profileAddress, introduction);
    }

}
