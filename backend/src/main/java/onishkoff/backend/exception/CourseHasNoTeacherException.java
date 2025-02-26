package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class CourseHasNoTeacherException extends BaseException {
    public CourseHasNoTeacherException() {
        super("На данном курсе не назначен учитель", HttpStatus.BAD_REQUEST);
    }
}
