package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchRequestException extends BaseException {
    public NoSuchRequestException() {
        super("Такого запроса нет", HttpStatus.NOT_FOUND);
    }
}
