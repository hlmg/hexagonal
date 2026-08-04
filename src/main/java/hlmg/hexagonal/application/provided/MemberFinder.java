package hlmg.hexagonal.application.provided;

import hlmg.hexagonal.domain.Member;

public interface MemberFinder {

    Member find(Long memberId);

}
