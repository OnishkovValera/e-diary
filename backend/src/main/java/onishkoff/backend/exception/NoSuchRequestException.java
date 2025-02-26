package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchRequestException extends BaseException {
    public NoSuchRequestException() {
        super("No such request", HttpStatus.NOT_FOUND);
    }
}
