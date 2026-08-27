package hlmg.hexagonal.domain.enrollment;

import hlmg.hexagonal.domain.course.CourseFixture;
import hlmg.hexagonal.domain.member.MemberFixture;
import org.instancio.Instancio;

import java.time.LocalDateTime;

import static org.instancio.Select.field;

public class EnrollmentFixture {

    public static Enrollment createEnrollment() {
        return Enrollment.enroll(MemberFixture.createActiveMember(), CourseFixture.createActiveCourse());
    }

    public static Enrollment createEnrollment(EnrollmentStatus status) {
        return Instancio.of(Enrollment.class)
                .ignore(field(Enrollment::getId))
                .set(field(Enrollment::getEnrollmentStatus), status)
                .supply(field(Enrollment::getMember), MemberFixture::createActiveMember)
                .supply(field(Enrollment::getCourse), CourseFixture::createActiveCourse)
                .supply(field(Enrollment::getEnrolledAt), () -> LocalDateTime.now())
                .create();
    }

}
