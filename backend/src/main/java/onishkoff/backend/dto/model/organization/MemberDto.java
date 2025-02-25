package onishkoff.backend.dto.model.organization;

import lombok.*;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.model.enums.Role;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    Long id;
    UserDto member;
    Role roleInOrganization;
    LocalDateTime joinedAt;
}
