package hlmg.hexagonal.domain.member;

import hlmg.hexagonal.domain.shared.AbstractEntity;
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

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    void activate() {
        state(activatedAt == null, "Activated at is already set");

        this.activatedAt = LocalDateTime.now();
    }

    void deactivate() {
        state(deactivatedAt == null, "Deactivated at is already set");

        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberUpdateInfo updateInfo) {
        this.profile = convertToProfile(updateInfo.profileAddress());
        this.introduction = requireNonNull(updateInfo.introduction());
    }

    private @Nullable Profile convertToProfile(String profileAddress) {
        if (profileAddress.isEmpty()) {
            return null;
        }
        return new Profile(profileAddress);
    }

}
