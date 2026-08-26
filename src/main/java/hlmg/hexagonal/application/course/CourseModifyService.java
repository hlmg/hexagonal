package hlmg.hexagonal.application.course;

import hlmg.hexagonal.application.course.provided.*;
import hlmg.hexagonal.application.course.required.CourseRepository;
import hlmg.hexagonal.application.instructor.provided.InstructorFinder;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.support.exception.ValidationException;
import hlmg.hexagonal.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator {

    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;
    private final InstructorFinder instructorFinder;
    private final CourseValidator courseValidator;

    @Override
    public Course create(CourseCreateRequest createRequest) throws ValidationException {
        Instructor instructor = instructorFinder.find(createRequest.instructorId());

        courseValidator.validateForCreate(instructor, createRequest);

        Course course = new Course(instructor, createRequest.title(), createRequest.description());

        return courseRepository.save(course);
    }

    @Override
    public Course update(CourseInfoUpdateRequest infoUpdateRequest, Long courseId) throws ValidationException {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForUpdate(course, infoUpdateRequest);

        course.updateInfo(infoUpdateRequest.toInfo());

        return courseRepository.save(course);
    }

}
