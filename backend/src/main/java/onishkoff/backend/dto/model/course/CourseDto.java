package onishkoff.backend.dto.model.course;


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
    String name;
    UserDto teacher;
    LocalDateTime createdAt;
    Set<UserDto> students;
}
