package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.dto.auth.TokenResponse;
import onishkoff.backend.dto.auth.UserCredentialsDto;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.exception.auth.WrongPasswordException;
import onishkoff.backend.model.User;
import onishkoff.backend.utils.JwtUtil;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtTokenUtils;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;


    public TokenResponse authenticate(UserCredentialsDto credentials) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword()));
        }catch (BadCredentialsException exception){
            throw new WrongPasswordException();
        }
        User user = userService.findByLogin(credentials.getLogin());
        return  new TokenResponse(jwtTokenUtils.generateToken(user), modelMapper.map(user, UserDto.class));


    }

    public UserDto register(RegistrationDto registerDto) {
        return modelMapper.map(userService.registerUser(registerDto), UserDto.class);
    }

    public UserDto check() {
        return modelMapper.map(securityUtil.getUserFromContext(), UserDto.class);
    }
}
