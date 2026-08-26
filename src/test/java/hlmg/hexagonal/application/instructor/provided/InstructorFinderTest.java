package hlmg.hexagonal.application.instructor.provided;

import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import hlmg.hexagonal.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorFinderTest extends BaseApplicationServiceTest {

    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;

    @Test
    void find() {
        Member member = prepareMember();
        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();

        assertThat(found).isEqualTo(instructor);
        assertThat(instructorFinder.findByMember(Long.MAX_VALUE)).isEmpty();
    }

}
