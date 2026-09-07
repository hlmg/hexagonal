package hlmg.hexagonal.application.enrollment.required;

import hlmg.hexagonal.domain.enrollment.Enrollment;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends Repository<Enrollment, Long> {

    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(Long enrollmentId);

    List<Enrollment> findByMemberId(Long memberId);

    Optional<Enrollment> findByMemberIdAndCourseId(Long memberId, Long courseId);

}
