package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchCourse extends BaseException {
    public NoSuchCourse() {
        super("Такой группы не существует", HttpStatus.NOT_FOUND);
    }
}
