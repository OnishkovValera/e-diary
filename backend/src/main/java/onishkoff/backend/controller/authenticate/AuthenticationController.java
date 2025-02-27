package onishkoff.backend.controller.authenticate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.dto.auth.TokenResponse;
import onishkoff.backend.dto.auth.UserCredentialsDto;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.model.User;
import onishkoff.backend.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody UserCredentialsDto user) {
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }

    @PostMapping("/reg")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegistrationDto registration) {
        return ResponseEntity.ok(authenticationService.register(registration));
    }
}
