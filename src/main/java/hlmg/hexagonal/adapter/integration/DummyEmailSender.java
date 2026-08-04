package hlmg.hexagonal.adapter.integration;

import hlmg.hexagonal.application.required.EmailSender;
import hlmg.hexagonal.domain.Email;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;

@Fallback
@Component
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }

}
