package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchOrganization extends BaseException {
    public NoSuchOrganization() {
        super("Такой организации не существует", HttpStatus.NOT_FOUND);
    }
}
