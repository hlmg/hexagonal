package hlmg.hexagonal.application.enrollment;

import hlmg.hexagonal.application.course.provided.CourseFinder;
import hlmg.hexagonal.application.enrollment.provided.EnrollRequest;
import hlmg.hexagonal.application.enrollment.provided.Enroller;
import hlmg.hexagonal.application.enrollment.provided.EnrollmentFinder;
import hlmg.hexagonal.application.enrollment.required.EnrollmentRepository;
import hlmg.hexagonal.application.member.provided.MemberFinder;
import hlmg.hexagonal.domain.course.Course;
import hlmg.hexagonal.domain.enrollment.Enrollment;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class EnrollmentModifyService implements Enroller {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentFinder enrollmentFinder;
    private final MemberFinder memberFinder;
    private final CourseFinder courseFinder;

    @Override
    public Enrollment enroll(EnrollRequest enrollRequest) {
        Member member = memberFinder.find(enrollRequest.memberId());
        Course course = courseFinder.find(enrollRequest.courseId());

        checkDuplication(member, course);

        Enrollment enrollment = Enrollment.enroll(member, course);

        return enrollmentRepository.save(enrollment);
    }

    private void checkDuplication(Member member, Course course) {
        enrollmentRepository.findByMemberIdAndCourseId(member.getId(), course.getId())
                .ifPresent(_ -> {
                    throw new IllegalArgumentException("Already enrolled");
                });
    }

    @Override
    public Enrollment startStudying(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.startStudying();

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment completeStudying(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.completeStudying();

        return enrollmentRepository.save(enrollment);
    }

}
