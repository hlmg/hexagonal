package hlmg.hexagonal.support.test;

import hlmg.hexagonal.application.course.required.CourseRepository;
import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.course.CourseFixture;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationServiceTest
public class BaseApplicationServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    CourseRepository courseRepository;

    protected Member prepareMember() {
        return memberRepository.save(MemberFixture.createActiveMember());
    }

    protected Instructor prepareInstructor() {
        return instructorRepository.save(InstructorFixture.createActiveInstructor(prepareMember()));
    }

    protected Course prepareCourse() {
        return courseRepository.save(CourseFixture.createCourse(prepareInstructor()));
    }

}
