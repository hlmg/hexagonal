package hlmg.hexagonal.application.enrollment.provided;

import hlmg.hexagonal.domain.enrollment.Enrollment;
import jakarta.validation.Valid;

public interface Enroller {

    Enrollment enroll(@Valid EnrollRequest enrollRequest);

    Enrollment startStudying(Long enrollmentId);

    Enrollment completeStudying(Long enrollmentId);

}
