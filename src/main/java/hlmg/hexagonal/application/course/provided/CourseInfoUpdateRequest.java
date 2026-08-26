package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.CourseUpdateInfo;
import jakarta.validation.constraints.Size;

public record CourseInfoUpdateRequest(
        @Size(min = 2, max = 100) String title,
        @Size(max = 1000) String description
) {

    public CourseUpdateInfo toInfo() {
        return new CourseUpdateInfo(title, description);
    }

}
