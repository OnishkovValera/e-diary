package onishkoff.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import onishkoff.backend.dto.model.UserDto;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private UserDto user;
}
