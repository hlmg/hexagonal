package hlmg.hexagonal.application.instructor;

import hlmg.hexagonal.application.instructor.provided.DuplicateInstructorApplicationException;
import hlmg.hexagonal.application.instructor.provided.InstructorApplication;
import hlmg.hexagonal.application.instructor.provided.InstructorApplyRequest;
import hlmg.hexagonal.application.instructor.provided.InstructorFinder;
import hlmg.hexagonal.application.instructor.required.InstructorRepository;
import hlmg.hexagonal.application.member.provided.MemberFinder;
import hlmg.hexagonal.domain.instructor.Instructor;
import hlmg.hexagonal.domain.member.Member;
import hlmg.hexagonal.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class InstructorModifyService implements InstructorApplication {

    private final InstructorRepository instructorRepository;
    private final InstructorFinder instructorFinder;
    private final MemberFinder memberFinder;

    @Override
    public Instructor apply(InstructorApplyRequest applyRequest) {
        Member member = memberFinder.find(applyRequest.memberId());
        checkDuplicateApplication(member);

        Instructor instructor = Instructor.apply(member);

        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor approve(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);

        instructor.approve();

        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor reject(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);

        instructor.reject();

        return instructorRepository.save(instructor);
    }

    private void checkDuplicateApplication(Member member) {
        if (instructorRepository.findByMemberId(member.getId()).isPresent()) {
            throw new DuplicateInstructorApplicationException("Instructor already exists");
        }
    }

}
