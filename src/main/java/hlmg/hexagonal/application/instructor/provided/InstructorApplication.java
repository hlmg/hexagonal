package hlmg.hexagonal.application.instructor.provided;

import hlmg.hexagonal.domain.instructor.Instructor;
import jakarta.validation.Valid;

public interface InstructorApplication {

    Instructor apply(@Valid InstructorApplyRequest applyRequest);

    Instructor approve(Long instructorId);

    Instructor reject(Long instructorId);

}
