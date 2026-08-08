package hlmg.hexagonal.application.member;

import hlmg.hexagonal.application.member.provided.MemberFinder;
import hlmg.hexagonal.application.member.provided.MemberRegister;
import hlmg.hexagonal.application.member.required.EmailSender;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.member.*;
import hlmg.hexagonal.domain.shared.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Validated
@Service
public class MemberModifyService implements MemberRegister {

    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberFinder.find(memberId);

        checkDuplicateProfile(member, memberInfoUpdateRequest.profileAddress());

        member.updateInfo(memberInfoUpdateRequest);

        return memberRepository.save(member);
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("Email already exist");
        }
    }

    private void checkDuplicateProfile(Member member, String profileAddress) {
        if (profileAddress.isEmpty()) return;
        if (isSameProfileAddress(member.getDetail().getProfile(), profileAddress)) return;

        if (memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("Profile address already exist");
        }
    }

    private boolean isSameProfileAddress(Profile profile, String profileAddress) {
        return Optional.ofNullable(profile)
                .map(Profile::address)
                .map(address -> address.equals(profileAddress))
                .orElse(false);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "Activate your account", "To complete your registration and activate your account, please click the link below");
    }

}
