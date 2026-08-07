package hlmg.hexagonal.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MemberDetail extends AbstractEntity {

    private @Nullable Profile profile;

    private @Nullable String introduction;

    private LocalDateTime registeredAt;

    private @Nullable LocalDateTime activatedAt;

    private @Nullable LocalDateTime deactivatedAt;

    public static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    public void activate() {
        state(activatedAt == null, "Activated at is already set");

        this.activatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        state(deactivatedAt == null, "Deactivated at is already set");

        this.deactivatedAt = LocalDateTime.now();
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = requireNonNull(updateRequest.introduction());
    }

}
