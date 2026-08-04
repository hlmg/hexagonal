package hlmg.hexagonal;

import hlmg.hexagonal.application.required.EmailSender;
import hlmg.hexagonal.domain.MemberFixture;
import hlmg.hexagonal.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class SimpleTestConfiguration {

    @Primary
    @Bean
    public EmailSender testEmailSender() {
        return (email, _, _) -> System.out.println("Sending email to: " + email);
    }

    @Primary
    @Bean
    public PasswordEncoder testPasswordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }

}
