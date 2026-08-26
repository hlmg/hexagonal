package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.support.exception.ValidationException;
import jakarta.validation.Valid;

public interface CourseCreator {

    Course create(@Valid CourseCreateRequest createRequest) throws ValidationException;

    Course update(@Valid CourseInfoUpdateRequest infoUpdateRequest, Long courseId) throws ValidationException;

}
