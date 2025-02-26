package onishkoff.backend.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    Long id;
    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
    String firstName;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
    String lastName;
    LocalDateTime createdAt;
}


