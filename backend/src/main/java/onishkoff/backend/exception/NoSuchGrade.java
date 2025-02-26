package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchGrade extends BaseException {
    public NoSuchGrade() {
        super("Такой оценки не существует", HttpStatus.NOT_FOUND);
    }
}
