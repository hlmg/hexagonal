package hlmg.hexagonal.adapter.webapi;

import hlmg.hexagonal.adapter.webapi.dto.MemberRegisterResponse;
import hlmg.hexagonal.application.member.provided.MemberRegister;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
class MemberApi {

    private final MemberRegister memberRegister;

    @PostMapping
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }

}
