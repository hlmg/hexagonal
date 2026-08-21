package hlmg.hexagonal.adapter.webapi.dto;

import hlmg.hexagonal.domain.member.Member;

public record MemberRegisterResponse(Long memberId, String email) {

    public static MemberRegisterResponse from(Member member) {
        return new MemberRegisterResponse(member.getId(), member.getEmail().address());
    }

}
