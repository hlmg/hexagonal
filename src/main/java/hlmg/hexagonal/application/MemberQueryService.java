package hlmg.hexagonal.application;

import hlmg.hexagonal.application.provided.MemberFinder;
import hlmg.hexagonal.application.required.MemberRepository;
import hlmg.hexagonal.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
@Service
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
    }

}
