package hlmg.hexagonal.adapter.security;

import hlmg.hexagonal.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurePasswordEncoder implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder delegate;

    @Override
    public String encode(String password) {
        return delegate.encode(password);
    }

    @Override
    public boolean matches(String password, String passwordHash) {
        return delegate.matches(password, passwordHash);
    }

}
