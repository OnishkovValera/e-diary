package onishkoff.backend.dto.model;

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
    String firstName;
    String lastName;
    LocalDateTime created_at;
}


