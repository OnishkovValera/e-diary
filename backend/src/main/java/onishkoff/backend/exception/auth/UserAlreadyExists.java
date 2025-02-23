package onishkoff.backend.exception.auth;

import onishkoff.backend.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExists extends BaseException {
    public UserAlreadyExists() {
        super("Такой пользователь уже существует", HttpStatus.BAD_REQUEST);
    }
}
