package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    HttpStatus httpStatus;

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
