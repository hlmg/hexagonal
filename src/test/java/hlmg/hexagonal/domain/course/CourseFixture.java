package hlmg.hexagonal.domain.course;

import hlmg.hexagonal.application.course.provided.CourseCreateRequest;
import hlmg.hexagonal.application.course.provided.CourseInfoUpdateRequest;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import org.instancio.Instancio;
import org.instancio.Model;

import java.time.LocalDateTime;

import static org.instancio.Select.field;

public class CourseFixture {

    public static Model<Course> courseModel() {
        return Instancio.of(Course.class)
                .ignore(field(Course::getId))
                .supply(field(Course::getInstructor), () -> InstructorFixture.createActiveInstructor())
                .generate(field(Course::getTitle), gen -> gen.string().minLength(2).maxLength(100))
                .set(field(Course::getStatus), CourseStatus.DRAFT)
                .generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(1000))
                .supply(field(CourseDetail::getCreatedAt), () -> LocalDateTime.now())
                .toModel();
    }

    public static Course createCourse() {
        return Instancio.of(courseModel()).create();
    }

    public static Course createCourse(CourseStatus status) {
        return Instancio.of(courseModel())
                .set(field(Course::getStatus), status)
                .create();
    }

    public static Course createCourse(Instructor instructor) {
        return Instancio.of(courseModel())
                .set(field(Course::getInstructor), instructor)
                .create();
    }

    public static Course createCourse(Instructor instructor, String title) {
        return Instancio.of(courseModel())
                .set(field(Course::getInstructor), instructor)
                .set(field(Course::getTitle), title)
                .create();
    }

    public static Course createCourse(Instructor instructor, CourseStatus status) {
        return Instancio.of(courseModel())
                .set(field(Course::getInstructor), instructor)
                .set(field(Course::getStatus), status)
                .create();
    }

    public static Course createPublishedCourse() {
        return createCourse(CourseStatus.PUBLISHED);
    }

    public static CourseCreateRequest createCourseCreateRequest(Long instructorId) {
        return Instancio.of(CourseCreateRequest.class)
                .set(field(CourseCreateRequest::instructorId), instructorId)
                .generate(field(CourseCreateRequest::title), gen -> gen.string().minLength(2).maxLength(100))
                .generate(field(CourseCreateRequest::description), gen -> gen.string().maxLength(1000))
                .create();
    }

    public static CourseInfoUpdateRequest createCourseInfoUpdateRequest(String title) {
        return Instancio.of(CourseInfoUpdateRequest.class)
                .set(field(CourseInfoUpdateRequest::title), title)
                .generate(field(CourseInfoUpdateRequest::description), gen -> gen.string().maxLength(1000))
                .create();
    }

}
