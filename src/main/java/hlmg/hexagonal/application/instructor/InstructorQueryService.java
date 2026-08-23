package hlmg.hexagonal.application.instructor;

import hlmg.hexagonal.application.instructor.provided.InstructorFinder;
import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class InstructorQueryService implements InstructorFinder {

    private final InstructorRepository instructorRepository;

    @Override
    public Instructor find(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + instructorId));
    }

    @Override
    public Optional<Instructor> findByMember(Long memberId) {
        return instructorRepository.findByMemberId(memberId);
    }

}
