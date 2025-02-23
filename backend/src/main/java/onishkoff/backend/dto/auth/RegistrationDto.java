package onishkoff.backend.dto.auth;


import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.model.enums.Role;

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
    @Min(6)
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+", message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и цифру")
    String password;

    @NotNull
    Role role;

}
