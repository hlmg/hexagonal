package hlmg.hexagonal.application.member.provided;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.support.stereotype.ApplicationServiceTest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberFinderTest {

    final MemberFinder memberFinder;
    final MemberRegister memberRegister;
    final EntityManager entityManager;

    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(found.getId()).isEqualTo(member.getId());
    }

    @Test
    void findFailWhenNotFound() {
        assertThatThrownBy(() -> memberFinder.find(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
