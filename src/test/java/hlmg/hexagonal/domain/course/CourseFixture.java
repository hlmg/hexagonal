package hlmg.hexagonal.domain.course;

import hlmg.hexagonal.application.course.provided.CourseCreateRequest;
import hlmg.hexagonal.application.course.provided.CourseInfoUpdateRequest;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import org.instancio.Instancio;

import java.time.LocalDateTime;

import static org.instancio.Instancio.gen;
import static org.instancio.Select.field;

public class CourseFixture {

    public static Course createCourse() {
        return createCourse(CourseStatus.DRAFT, null, null);
    }

    public static Course createCourse(CourseStatus status) {
        return createCourse(status, null, null);
    }

    public static Course createCourse(Instructor instructor) {
        return createCourse(CourseStatus.DRAFT, instructor, null);
    }

    public static Course createCourse(Instructor instructor, String title) {
        return createCourse(CourseStatus.DRAFT, instructor, title);
    }

    public static Course createCourse(CourseStatus status, Instructor instructor, String title) {
        CourseDetail courseDetail = Instancio.of(CourseDetail.class)
                .ignore(field(Course::getId))
                .generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(1000))
                .set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
                .create();

        return Instancio.of(Course.class)
                .ignore(field(Course::getId))
                .set(field(Course::getInstructor), instructor == null ? InstructorFixture.createActiveInstructor() : instructor)
                .set(field(Course::getTitle), title == null ? gen().string().minLength(2).maxLength(100).get() : title)
                .set(field(Course::getStatus), status)
                .set(field(Course::getDetail), courseDetail)
                .create();
    }

    public static CourseCreateRequest createCourseCreateRequest(Long instructorId, String title) {
        return Instancio.of(CourseCreateRequest.class)
                .set(field(CourseCreateRequest::instructorId), instructorId)
                .set(field(CourseCreateRequest::title), title == null ? gen().string().minLength(2).maxLength(100).get() : title)
                .generate(field(CourseCreateRequest::description), gen -> gen.string().maxLength(1000))
                .create();

    }

    public static CourseInfoUpdateRequest createCourseInfoUpdateRequest(String title) {
        return Instancio.of(CourseInfoUpdateRequest.class)
                .set(field(CourseInfoUpdateRequest::title), title == null ? gen().string().minLength(2).maxLength(100).get() : title)
                .generate(field(CourseInfoUpdateRequest::description), gen -> gen.string().maxLength(1000))
                .create();
    }

}
