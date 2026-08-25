package hlmg.hexagonal.domain.course;

import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

public record CourseUpdateInfo(
        @Size(min = 2, max = 100) String title,
        @Nullable String description
) {

}
