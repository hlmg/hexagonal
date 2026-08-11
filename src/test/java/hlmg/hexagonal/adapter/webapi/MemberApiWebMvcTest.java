package hlmg.hexagonal.adapter.webapi;

import hlmg.hexagonal.adapter.webapi.dto.MemberRegisterResponse;
import hlmg.hexagonal.application.member.provided.MemberRegister;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import hlmg.hexagonal.domain.member.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {

    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;

    @MockitoBean
    MemberRegister memberRegister;

    @Test
    void register() {
        Member member = MemberFixture.createMember(1L);
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();

        when(memberRegister.register(request)).thenReturn(member);
        String requestJson = objectMapper.writeValueAsString(request);

        MemberRegisterResponse response = mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .assertThat()
                .hasStatusOk()
                .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(MemberRegisterResponse.class).actual();

        Assertions.assertThat(response.memberId()).isEqualTo(member.getId());

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail() {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email");
        String requestJson = objectMapper.writeValueAsString(request);

        mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .assertThat()
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

}
