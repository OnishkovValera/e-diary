package onishkoff.backend.dto.model.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.dto.model.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OrganizationDto {
    Long id;
    @NotNull
    @NotBlank
    String name;
    UserDto owner;
    LocalDateTime createdAt;
    List<MemberDto> memberOrganizations;
}
