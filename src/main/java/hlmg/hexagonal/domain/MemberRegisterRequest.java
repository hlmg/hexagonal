package hlmg.hexagonal.domain;

public record MemberRegisterRequest(
        String email, String nickname, String password
) {

}
