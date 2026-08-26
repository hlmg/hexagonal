package hlmg.hexagonal.application.course.provided;

import hlmg.hexagonal.domain.course.Course;

import java.util.List;

public interface CourseFinder {

    Course find(Long courseId);

    List<Course> findByTitle(String keyword);

    List<Course> findByInstructorId(Long instructorId);

}
