package onishkoff.backend.exception.auth;

import onishkoff.backend.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends BaseException {

    public WrongPasswordException() {
        super("Wrong password exception", HttpStatus.UNAUTHORIZED);
    }
}
