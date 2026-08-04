package hlmg.hexagonal;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class HexagonalApplicationTest {

    @Test
    void run() {
        try (MockedStatic<SpringApplication> mock = Mockito.mockStatic(SpringApplication.class)) {
            HexagonalApplication.main(new String[0]);

            mock.verify(() -> SpringApplication.run(HexagonalApplication.class, new String[0]));
        }
    }

}
