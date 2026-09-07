package hlmg.hexagonal.application.enrollment.provided;

import jakarta.validation.constraints.NotNull;

public record EnrollRequest(@NotNull Long memberId, @NotNull Long courseId) {

}
