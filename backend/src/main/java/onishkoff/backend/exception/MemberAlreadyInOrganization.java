package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class MemberAlreadyInOrganization extends BaseException {
    public MemberAlreadyInOrganization() {
        super("Этот пользователь уже состоит в организации", HttpStatus.BAD_REQUEST);
    }
}
