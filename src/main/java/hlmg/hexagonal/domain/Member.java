package hlmg.hexagonal.domain;

import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Getter
@ToString
public final class Member {

    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member(Email email, String nickname, String passwordHash) {
        this.email = email;
        this.nickname = requireNonNull(nickname);
        this.passwordHash = requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    public static Member create(MemberCreateRequest createRequest, PasswordEncoder passwordEncoder) {
        return new Member(new Email(createRequest.email()), createRequest.nickname(), passwordEncoder.encode(createRequest.password()));
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "member is not pending");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "member is not active");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

}
