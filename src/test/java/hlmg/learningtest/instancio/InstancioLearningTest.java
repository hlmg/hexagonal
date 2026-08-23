package hlmg.learningtest.instancio;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@ExtendWith(InstancioExtension.class) // Seed logging
class InstancioLearningTest {

    @Test
    void basic() {
        User user = Instancio.of(User.class)
                .ignore(field(User::getId))
                .generate(field(User::getEmail), gen -> gen.net().email())
                .set(field(User::getStatus), UserStatus.PENDING)
                .create();

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isNotEmpty();
        assertThat(user.getName()).isNotEmpty();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    private final Model<User> baseUserModel = Instancio.of(User.class)
            .ignore(field(User::getId))
            .set(field(User::getStatus), UserStatus.PENDING)
            .toModel();

    //    @Seed(123L)
    @Test
    void model() {
        for (int i = 0; i < 10; i++) {
            User user = Instancio.of(baseUserModel)
                    .set(field(User::getName), "user" + i)
                    .create();

            assertThat(user.getName()).isEqualTo("user" + i);
        }
    }

    /*
    # instancio.properties
    bean.validation.enabled=true
     */
    @Test
    void validation() {
        UserRegisterRequest request = Instancio.of(UserRegisterRequest.class).create();

        assertThat(request.email()).isNotEmpty();
        assertThat(request.nickname()).hasSizeBetween(5, 20);
        assertThat(request.password()).hasSizeBetween(8, 20);
    }

}
