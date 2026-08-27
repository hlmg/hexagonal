package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.Course;

public interface CoursePublisher {

    Course submitForReview(Long courseId);

    Course publish(Long courseId);

    Course archive(Long courseId);

}
