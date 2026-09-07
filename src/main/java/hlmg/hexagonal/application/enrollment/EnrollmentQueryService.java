package hlmg.hexagonal.application.enrollment;

import hlmg.hexagonal.application.enrollment.provided.EnrollmentFinder;
import hlmg.hexagonal.application.enrollment.required.EnrollmentRepository;
import hlmg.hexagonal.domain.enrollment.Enrollment;
import hlmg.hexagonal.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class EnrollmentQueryService implements EnrollmentFinder {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment find(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));
    }

    @Override
    public List<Enrollment> findByMemberId(Long memberId) {
        return enrollmentRepository.findByMemberId(memberId);
    }

    @Override
    public Optional<Enrollment> findByMemberIdAndCourseId(Long memberId, Long courseId) {
        return enrollmentRepository.findByMemberIdAndCourseId(memberId, courseId);
    }

}
