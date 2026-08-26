package hlmg.hexagonal.domain.instructor;

import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.shared.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static hlmg.hexagonal.domain.instructor.InstructorStatus.ACTIVE;
import static hlmg.hexagonal.domain.instructor.InstructorStatus.PENDING;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InstructorStatus status;

    public static Instructor apply(Member member) {
        // TODO: Use member.ensure
        state(member.isActive(), "member must be active");

        Instructor instructor = new Instructor();
        instructor.member = member;
        instructor.status = PENDING;
        return instructor;
    }

    public void approve() {
        state(status == PENDING, "instructor status must be PENDING");

        status = InstructorStatus.ACTIVE;
    }

    public void reject() {
        state(status == PENDING, "instructor status must be PENDING");

        status = InstructorStatus.REJECTED;
    }

    public boolean isActive() {
        return status == ACTIVE;
    }

    public void ensureActive() {
        state(status == ACTIVE, "instructor status must be ACTIVE");
    }

}
