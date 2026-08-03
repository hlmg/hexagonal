package hlmg.hexagonal.application.provided;

import hlmg.hexagonal.domain.Member;
import hlmg.hexagonal.domain.MemberRegisterRequest;

public interface MemberRegister {

    Member register(MemberRegisterRequest registerRequest);

}
