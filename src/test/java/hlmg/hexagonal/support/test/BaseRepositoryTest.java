package hlmg.hexagonal.support.test;

import hlmg.hexagonal.application.course.required.CourseRepository;
import hlmg.hexagonal.application.enrollment.required.EnrollmentRepository;
import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.course.CourseFixture;
import hlmg.hexagonal.domain.course.CourseStatus;
import hlmg.hexagonal.domain.enrollment.Enrollment;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class BaseRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    protected Member member;
    protected Instructor instructor;
    protected Course course;

    protected Member prepareActiveMember() {
        member = memberRepository.save(MemberFixture.createActiveMember());
        return member;
    }

    protected Instructor prepareActiveInstructor(Member member) {
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));
        return instructor;
    }

    protected Instructor prepareActiveInstructor() {
        member = prepareActiveMember();
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));
        return instructor;
    }

    protected Course preparePublishedCourse() {
        instructor = prepareActiveInstructor();
        course = courseRepository.save(CourseFixture.createCourse(instructor, CourseStatus.PUBLISHED));
        return course;
    }

    protected Course prepareCourse() {
        return prepareCourse(null, null);
    }

    protected Course prepareCourse(Instructor instructor, String title) {
        if (instructor == null) prepareActiveInstructor();

        course = courseRepository.save(CourseFixture.createCourse(instructor == null ? this.instructor : instructor, title));
        return course;
    }

    protected Enrollment prepareEnrollment(Member member1, Course course1) {
        return enrollmentRepository.save(Enrollment.enroll(member1, course1));
    }

}
