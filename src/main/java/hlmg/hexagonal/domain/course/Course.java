package hlmg.hexagonal.domain.course;

import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.shared.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"instructor_id", "title"}))
@Getter
@ToString(callSuper = true, exclude = {"instructor", "detail"})
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Course extends AbstractEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Instructor instructor;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CourseDetail detail;

    public Course(Instructor instructor, String title, @Nullable String description) {
        instructor.ensureActive();

        this.instructor = instructor;
        this.title = requireNonNull(title);
        this.status = CourseStatus.DRAFT;
        this.detail = new CourseDetail(description);
    }

    public void submitForReview() {
        state(status == CourseStatus.DRAFT, "course status must be DRAFT");
        state(StringUtils.hasText(detail.getDescription()), "course description must not be empty");

        this.status = CourseStatus.IN_REVIEW;
    }

    public void publish() {
        state(status == CourseStatus.IN_REVIEW, "course status must be IN_REVIEW");

        this.status = CourseStatus.PUBLISHED;
        this.getDetail().publish();
    }

    public void archive() {
        state(status == CourseStatus.PUBLISHED, "course status must be PUBLISHED");

        this.status = CourseStatus.ARCHIVED;
        this.getDetail().archive();
    }

    public void updateInfo(CourseUpdateInfo updateInfo) {
        this.title = requireNonNull(updateInfo.title());
        this.detail.updateInfo(updateInfo);
    }

    public boolean isPublished() {
        return status == CourseStatus.PUBLISHED;
    }

    public void ensurePublished() {
        state(isPublished(), "course status must be PUBLISHED");
    }

}
