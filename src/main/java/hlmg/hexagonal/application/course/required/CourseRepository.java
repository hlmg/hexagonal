package hlmg.hexagonal.application.course.required;

import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.instructor.Instructor;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends Repository<Course, Long> {

    Course save(Course course);

    Optional<Course> findById(Long courseId);

    List<Course> findByTitleContaining(String keyword);

    List<Course> findByInstructorId(Long instructorId);

    Optional<Course> findByInstructorAndTitle(Instructor instructor, String title);

}
