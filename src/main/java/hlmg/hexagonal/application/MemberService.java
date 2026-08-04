package hlmg.hexagonal.application;

import hlmg.hexagonal.application.provided.MemberRegister;
import hlmg.hexagonal.application.required.EmailSender;
import hlmg.hexagonal.application.required.MemberRepository;
import hlmg.hexagonal.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@RequiredArgsConstructor
@Validated
@Service
public class MemberService implements MemberRegister {

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        member.activate();

        return memberRepository.save(member);
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("Email already exist");
        }
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "Activate your account", "To complete your registration and activate your account, please click the link below");
    }

}
