package hlmg.hexagonal.domain.enrollment;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.shared.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;

import static org.springframework.util.Assert.state;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_enrollment_member_course", columnNames = {"member_id", "course_id"})})
@Getter
@ToString(callSuper = true, exclude = {"member", "course"})
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Enrollment extends AbstractEntity {

    @NaturalId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @NaturalId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnrollmentStatus enrollmentStatus;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;

    public static Enrollment enroll(Member member, Course course) {
        member.ensureActive();
        course.ensurePublished();

        Enrollment enrollment = new Enrollment();
        enrollment.member = member;
        enrollment.course = course;
        enrollment.enrollmentStatus = EnrollmentStatus.ENROLLED;
        enrollment.enrolledAt = LocalDateTime.now();
        return enrollment;
    }

    public void startStudying() {
        ensureEnrollment();

        enrollmentStatus = EnrollmentStatus.STUDYING;
    }

    public void completeStudying() {
        state(enrollmentStatus == EnrollmentStatus.STUDYING, "enrollment status must be STUDYING");

        enrollmentStatus = EnrollmentStatus.COMPLETED;
        completedAt = LocalDateTime.now();
    }

    public void ensureEnrollment() {
        state(enrollmentStatus == EnrollmentStatus.ENROLLED, "enrollment status must be ENROLLED");
    }

}
