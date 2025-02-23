package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.exception.auth.WrongPasswordException;
import onishkoff.backend.model.User;
import onishkoff.backend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtTokenUtils;


    public String authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        }catch (BadCredentialsException exception){
            throw new WrongPasswordException();
        }
        User user = userService.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return jwtTokenUtils.generateToken(user);


    }

    public User register(RegistrationDto registerDto) {
        return userService.registerUser(registerDto);
    }

}
