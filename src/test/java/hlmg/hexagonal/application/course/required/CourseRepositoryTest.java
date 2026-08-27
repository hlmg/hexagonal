package hlmg.hexagonal.application.course.required;

import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.application.member.required.MemberRepository;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.instructor.InstructorFixture;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.domain.member.MemberFixture;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static hlmg.hexagonal.domain.course.CourseFixture.createCourse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class CourseRepositoryTest {

    final CourseRepository courseRepository;
    final MemberRepository memberRepository;
    final InstructorRepository instructorRepository;

    Member member;
    Instructor instructor;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.createActiveMember());
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));
    }

    @Test
    void save_ValidCourse_Success() {
        Course course = createCourse(instructor);

        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();
    }

    @Test
    void save_DuplicateTitleForSameInstructor_Fails() {
        courseRepository.save(createCourse(instructor, "title"));

        assertThatThrownBy(() -> courseRepository.save(createCourse(instructor, "title")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByTitleContaining_MatchingTitle_ReturnsMatchedCourses() {
        Course course1 = courseRepository.save(createCourse(instructor, "Spring Basic"));
        Course course2 = courseRepository.save(createCourse(instructor, "Spring Intermediate"));
        Course course3 = courseRepository.save(createCourse(instructor, "Java Basic"));

        assertFindByTitle("Spring", course1, course2);
        assertFindByTitle("Basic", course1, course3);
    }

    @Test
    void findByTitleContaining_NonMatchingTitle_ReturnsEmptyList() {
        courseRepository.save(createCourse(instructor, "Spring Basic"));

        assertFindByTitle("No Course");
    }

    void assertFindByTitle(String keyword, Course... expectedCourses) {
        assertThat(courseRepository.findByTitleContaining(keyword))
                .containsExactlyInAnyOrder(expectedCourses);
    }

    @Test
    void findByInstructorId_ExistingInstructor_ReturnsInstructorCourses() {
        Member member2 = memberRepository.save(MemberFixture.createActiveMember());
        Instructor instructor2 = instructorRepository.save(InstructorFixture.createActiveInstructor(member2));
        Course course1 = courseRepository.save(createCourse(instructor, "Spring Basic"));
        Course course2 = courseRepository.save(createCourse(instructor, "Spring Advanced"));
        Course course3 = courseRepository.save(createCourse(instructor2, "Java Basic"));

        assertFindByInstructorId(instructor.getId(), course1, course2);
        assertFindByInstructorId(instructor2.getId(), course3);
    }

    @Test
    void findByInstructorId_NonExistingInstructor_ReturnsEmptyList() {
        assertFindByInstructorId(-1L);
    }

    void assertFindByInstructorId(Long instructorId, Course... expectedCourses) {
        assertThat(courseRepository.findByInstructorId(instructorId))
                .containsExactlyInAnyOrder(expectedCourses);
    }

}
