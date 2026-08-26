package hlmg.hexagonal.application.course;

import hlmg.hexagonal.application.course.provided.CourseFinder;
import hlmg.hexagonal.application.course.required.CourseRepository;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class CourseQueryService implements CourseFinder {

    private final CourseRepository courseRepository;

    @Override
    public Course find(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
    }

    @Override
    public List<Course> findByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }

    @Override
    public List<Course> findByInstructorId(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

}
