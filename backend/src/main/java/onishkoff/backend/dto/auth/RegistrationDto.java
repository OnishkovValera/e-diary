package onishkoff.backend.dto.auth;


import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationDto {

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    @Email
    String login;

    @NotNull
    @Size(min = 6)
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+", message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и цифру")
    String password;



}
