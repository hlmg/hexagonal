package hlmg.hexagonal.application.member;

import hlmg.hexagonal.application.member.provided.MemberFinder;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
    }

}
