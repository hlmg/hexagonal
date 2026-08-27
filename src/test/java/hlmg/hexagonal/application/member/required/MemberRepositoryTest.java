package hlmg.hexagonal.application.member.required;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberRegisterInfo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static hlmg.hexagonal.domain.member.MemberFixture.createMemberRegisterRequest;
import static hlmg.hexagonal.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class MemberRepositoryTest {

    final MemberRepository memberRepository;

    @Test
    void save_ValidMember_Success() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getDetail().getId()).isNotNull();
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void save_DuplicateEmail_ThrowsDataIntegrityViolationException() {
        MemberRegisterInfo registerInfo = createMemberRegisterRequest().toInfo();
        Member member = Member.register(registerInfo, createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(registerInfo, createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
