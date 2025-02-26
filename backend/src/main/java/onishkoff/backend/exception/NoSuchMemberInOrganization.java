package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchMemberInOrganization extends BaseException {
    public NoSuchMemberInOrganization() {
        super("Такого пользователя нет в организации", HttpStatus.NOT_FOUND);
    }
}
