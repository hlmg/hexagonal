package hlmg.hexagonal.application.required;

import hlmg.hexagonal.domain.Email;
import hlmg.hexagonal.domain.Member;
import hlmg.hexagonal.domain.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId);

    @Query("select m from Member m where m.detail.profile = :profile")
    Optional<Member> findByProfile(Profile profile);

}
