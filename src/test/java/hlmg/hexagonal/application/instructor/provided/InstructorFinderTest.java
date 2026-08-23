package hlmg.hexagonal.application.instructor.provided;

import hlmg.hexagonal.application.member.provided.MemberRegister;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorFinderTest {

    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;
    final MemberRegister memberRegister;

    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        memberRegister.activate(member.getId());
        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();

        assertThat(found).isEqualTo(instructor);
        assertThat(instructorFinder.findByMember(Long.MAX_VALUE)).isEmpty();
    }

}
