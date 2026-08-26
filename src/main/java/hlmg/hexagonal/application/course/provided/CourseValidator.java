package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.exception.ValidationException;

public interface CourseValidator {

    void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException;

    void validateForUpdate(Course course, CourseInfoUpdateRequest infoUpdateRequest) throws ValidationException;

}
