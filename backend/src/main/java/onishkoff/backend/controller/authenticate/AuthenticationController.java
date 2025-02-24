package onishkoff.backend.controller.authenticate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.dto.auth.UserCredentialsDto;
import onishkoff.backend.model.User;
import onishkoff.backend.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public String authenticate(@RequestBody UserCredentialsDto user) {
        return authenticationService.authenticate(user);
    }

    @PostMapping("/reg")
    public User register(@Valid @RequestBody RegistrationDto registration) {
        return authenticationService.register(registration);
    }

}
