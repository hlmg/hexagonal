package hlmg.hexagonal.domain.course;

import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import org.instancio.Instancio;

import java.time.LocalDateTime;

import static org.instancio.Select.field;

public class CourseFixture {

    public static Course createCourse() {
        return createCourse(CourseStatus.DRAFT);
    }

    public static Course createCourse(CourseStatus status) {
        Instructor instructor = InstructorFixture.createActiveInstructor();

        CourseDetail courseDetail = Instancio.of(CourseDetail.class)
                .generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(1000))
                .set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
                .create();

        return Instancio.of(Course.class)
                .ignore(field(Course::getId))
                .set(field(Course::getInstructor), instructor)
                .generate(field(Course::getTitle), gen -> gen.string().minLength(2).maxLength(100))
                .set(field(Course::getStatus), status)
                .set(field(Course::getDetail), courseDetail)
                .create();
    }

}
