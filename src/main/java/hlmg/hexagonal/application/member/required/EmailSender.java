package hlmg.hexagonal.application.member.required;

import hlmg.hexagonal.domain.shared.Email;

public interface EmailSender {

    void send(Email email, String subject, String body);

}
