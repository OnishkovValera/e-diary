package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchStudentException extends BaseException {
    public NoSuchStudentException() {
        super("Такого студента нет в курсе", HttpStatus.NOT_FOUND);
    }
}
