package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.Member;

public interface MemberFinder {

    Member find(Long memberId);

}
