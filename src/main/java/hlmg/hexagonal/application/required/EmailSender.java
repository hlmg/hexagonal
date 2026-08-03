package hlmg.hexagonal.application.required;

import hlmg.hexagonal.domain.Email;

public interface EmailSender {

    void send(Email email, String subject, String body);

}
