package hlmg.hexagonal.application.course;

import hlmg.hexagonal.application.course.provided.CourseCreateRequest;
import hlmg.hexagonal.application.course.provided.CourseInfoUpdateRequest;
import hlmg.hexagonal.application.course.provided.CourseValidator;
import hlmg.hexagonal.application.course.required.CourseRepository;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.exception.ValidationException;
import hlmg.hexagonal.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class CourseValidatorService implements CourseValidator {

    private final CourseRepository courseRepository;

    @Override
    public void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException {
        instructor.ensureActive();

        List<String> errors = new ArrayList<>();

        checkTitleDuplicationForCreate(instructor, createRequest.title(), errors);
        checkBannedWords(createRequest.title(), errors);
        checkBannedWords(createRequest.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForUpdate(Course course, CourseInfoUpdateRequest infoUpdateRequest) throws ValidationException {
        List<String> errors = new ArrayList<>();

        checkTitleDuplicationForUpdate(course, infoUpdateRequest.title(), errors);
        checkBannedWords(infoUpdateRequest.title(), errors);
        checkBannedWords(infoUpdateRequest.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void checkTitleDuplicationForUpdate(Course course, String title, List<String> errors) {
        courseRepository.findByInstructorAndTitle(course.getInstructor(), title)
                .filter(found -> found != course)
                .ifPresent(_ -> errors.add("Title already exists: " + title));
    }

    private void checkTitleDuplicationForCreate(Instructor instructor, String title, List<String> errors) {
        if (courseRepository.findByInstructorAndTitle(instructor, title).isPresent()) {
            errors.add("Title already exists: " + title);
        }
    }

    private void checkBannedWords(String text, List<String> errors) {
        // TODO
    }

}
