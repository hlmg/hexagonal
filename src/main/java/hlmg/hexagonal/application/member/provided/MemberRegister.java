package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberInfoUpdateRequest;
import hlmg.hexagonal.domain.member.MemberRegisterRequest;
import jakarta.validation.Valid;

public interface MemberRegister {

    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);

    Member deactivate(Long memberId);

    Member updateInfo(Long memberId, @Valid MemberInfoUpdateRequest memberInfoUpdateRequest);

}
