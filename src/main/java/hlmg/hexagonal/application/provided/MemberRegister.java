package hlmg.hexagonal.application.provided;

import hlmg.hexagonal.domain.Member;
import hlmg.hexagonal.domain.MemberRegisterRequest;
import jakarta.validation.Valid;

public interface MemberRegister {

    Member register(@Valid MemberRegisterRequest registerRequest);

}
