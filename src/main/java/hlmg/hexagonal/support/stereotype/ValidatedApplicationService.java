package hlmg.hexagonal.support.stereotype;

import org.springframework.validation.annotation.Validated;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApplicationService
@Validated
public @interface ValidatedApplicationService {

}
