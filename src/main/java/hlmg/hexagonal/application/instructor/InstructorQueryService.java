package hlmg.hexagonal.application.instructor;

import hlmg.hexagonal.application.instructor.provided.InstructorFinder;
import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.domain.instructor.Instructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
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
