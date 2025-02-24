package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super("Такого пользователя нет в базе данных", HttpStatus.NOT_FOUND);
    }
}
