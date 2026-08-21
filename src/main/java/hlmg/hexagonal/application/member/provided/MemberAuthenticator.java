package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.Member;
import jakarta.validation.Valid;

public interface MemberAuthenticator {

    Member login(@Valid MemberLoginRequest loginRequest) throws LoginFailedException;

}
