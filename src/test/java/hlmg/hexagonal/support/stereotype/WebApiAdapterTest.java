package hlmg.hexagonal.support.stereotype;

import hlmg.hexagonal.SimpleTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Import(SimpleTestConfiguration.class)
public @interface WebApiAdapterTest {

}
