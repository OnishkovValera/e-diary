package onishkoff.backend.controller.GlobalExceptionHandler;

import onishkoff.backend.exception.BaseException;
import onishkoff.backend.exception.auth.UserAlreadyExists;
import onishkoff.backend.exception.auth.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(BaseException.class)
    public ProblemDetail handleUserAlreadyExists(BaseException ex) {
        return ProblemDetail.forStatusAndDetail(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ProblemDetail handleWrongPasswordException(WrongPasswordException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Неправильный пароль");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getBindingResult().getFieldError().getDefaultMessage());
    }


}
