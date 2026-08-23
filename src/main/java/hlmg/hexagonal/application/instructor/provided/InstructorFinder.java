package hlmg.hexagonal.application.instructor.provided;

import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.member.Member;

import java.util.Optional;

public interface InstructorFinder {

    Instructor find(Long instructorId);

    Optional<Instructor> findByMember(Long memberId);

    default Optional<Instructor> findByMember(Member member) {
        return findByMember(member.getId());
    }

}
