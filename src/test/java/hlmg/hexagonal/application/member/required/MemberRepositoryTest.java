package hlmg.hexagonal.application.member.required;

import hlmg.hexagonal.domain.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static hlmg.hexagonal.domain.member.MemberFixture.createMemberRegisterRequest;
import static hlmg.hexagonal.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
record MemberRepositoryTest(MemberRepository memberRepository, EntityManager entityManager) {

    @Test
    void createMember() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getDetail().getId()).isNotNull();
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void createFailWhenEmailAlreadyExist() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
