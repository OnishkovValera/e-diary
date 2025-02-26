package onishkoff.backend.dto.model.grade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeDto {

    Long id;

    @NotNull
    CourseDto course;

    @NotNull
    UserDto student;

    UserDto teacher;

    @Max(100)
    @Min(0)
    Long grade;

    LocalDateTime gradeDateTime;

    String comment;
}
