package hlmg.hexagonal.support.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<String> errors;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

}
