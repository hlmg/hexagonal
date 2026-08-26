package hlmg.hexagonal.domain.instructor;

import hlmg.hexagonal.application.instructor.provided.InstructorApplyRequest;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;

public class InstructorFixture {

    public static Instructor createInstructor(Member member) {
        return Instructor.apply(member);
    }

    public static Instructor createInstructor() {
        return createInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createActiveInstructor() {
        return createActiveInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createActiveInstructor(Member member) {
        Instructor instructor = createInstructor(member);
        instructor.approve();
        return instructor;
    }

    public static InstructorApplyRequest createApplyRequest(Member member) {
        return new InstructorApplyRequest(member.getId());
    }

}
