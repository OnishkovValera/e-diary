package onishkoff.backend.dto.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.model.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    Long id;

    @NotNull
    UserDto user;

    @NotNull
    OrganizationDto organization;
    CourseDto course;

    @NotNull
    Role role;
}
