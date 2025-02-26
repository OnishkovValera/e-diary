package onishkoff.backend.exception;

import org.springframework.http.HttpStatus;

public class WrongStatusException extends BaseException {
  public WrongStatusException() {
    super("Неверный статус обработки заявки", HttpStatus.BAD_REQUEST);
  }
}
