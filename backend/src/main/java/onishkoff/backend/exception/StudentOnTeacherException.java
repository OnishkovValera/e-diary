package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class StudentOnTeacherException extends BaseException {
    public StudentOnTeacherException() {
        super("Данный пользователь является учеником и не может быть назначен на роль учителя", HttpStatus.BAD_REQUEST);
    }
}
