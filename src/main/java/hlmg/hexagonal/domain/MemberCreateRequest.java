package hlmg.hexagonal.domain;

public record MemberCreateRequest(
        String email, String nickname, String password
) {

}
