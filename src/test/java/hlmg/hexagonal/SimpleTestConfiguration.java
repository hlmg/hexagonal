package hlmg.hexagonal;

import hlmg.hexagonal.application.required.EmailSender;
import hlmg.hexagonal.domain.MemberFixture;
import hlmg.hexagonal.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SimpleTestConfiguration {

    @Bean
    public EmailSender emailSender() {
        return (email, _, _) -> System.out.println("Sending email to: " + email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }

}
