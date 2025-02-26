package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class StudentNotSelectCourseException extends BaseException {
    public StudentNotSelectCourseException() {
        super("Студент обязан выбрать курс на который хочет записаться", HttpStatus.BAD_REQUEST);
    }
}
