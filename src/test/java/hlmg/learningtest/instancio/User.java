package hlmg.learningtest.instancio;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
class User {

    private Long id;
    private String name;
    private String email;
    private UserStatus status;

}
