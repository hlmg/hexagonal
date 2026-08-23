package hlmg.hexagonal.application.member;

import hlmg.hexagonal.application.member.provided.LoginFailedException;
import hlmg.hexagonal.application.member.provided.MemberAuthenticator;
import hlmg.hexagonal.application.member.provided.MemberLoginRequest;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.PasswordEncoder;
import hlmg.hexagonal.domain.shared.Email;
import hlmg.hexagonal.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
class MemberAuthenticationService implements MemberAuthenticator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginRequest loginRequest) throws LoginFailedException {
        Member member = memberRepository.findByEmail(new Email(loginRequest.email())).orElseThrow(LoginFailedException::new);

        if (!member.isActive()) {
            throw new LoginFailedException();
        }

        if (!member.verifyPassword(loginRequest.password(), passwordEncoder)) {
            throw new LoginFailedException();
        }

        return member;
    }

}
