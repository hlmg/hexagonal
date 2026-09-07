package hlmg.hexagonal.application.enrollment.provided;

import hlmg.hexagonal.domain.enrollment.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentFinder {

    Enrollment find(Long enrollmentId);

    List<Enrollment> findByMemberId(Long memberId);

    Optional<Enrollment> findByMemberIdAndCourseId(Long memberId, Long courseId);

}
