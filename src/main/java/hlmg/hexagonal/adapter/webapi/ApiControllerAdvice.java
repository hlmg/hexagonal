package hlmg.hexagonal.adapter.webapi;

import hlmg.hexagonal.domain.member.DuplicateEmailException;
import hlmg.hexagonal.domain.member.DuplicateProfileException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ProblemDetail handleException(Exception exception) {
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicateProfileException.class})
    public ProblemDetail handleConflictException(Exception exception) {
        return getProblemDetail(HttpStatus.CONFLICT, exception);
    }

    private static @NonNull ProblemDetail getProblemDetail(HttpStatus internalServerError, Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(internalServerError, exception.getMessage());
        problemDetail.setProperties(Map.of(
                "timestamp", LocalDateTime.now(),
                "exception", exception.getClass().getSimpleName()
        ));

        return problemDetail;
    }

}
