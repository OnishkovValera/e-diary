package onishkoff.backend.dto.model.course;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.dto.model.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CourseDto {
    Long id;

    @NotNull
    @NotBlank
    String name;
    UserDto teacher;
    LocalDateTime createdAt;
    Set<UserDto> students;
}
