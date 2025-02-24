package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.exception.UserNotFoundException;
import onishkoff.backend.exception.auth.UserAlreadyExists;
import onishkoff.backend.model.User;
import onishkoff.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public User registerUser(RegistrationDto registerDto) {
        userRepository.findByLogin(registerDto.getLogin()).ifPresent(user -> {
            throw new UserAlreadyExists();
        });
        return userRepository.save(modelMapper.map(registerDto, User.class));
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }



}
