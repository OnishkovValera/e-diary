package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.exception.auth.UserAlreadyExists;
import onishkoff.backend.model.User;
import onishkoff.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(RegistrationDto registerDto) {
        userRepository.findByLogin(registerDto.getLogin()).ifPresent(user -> {
            throw new UserAlreadyExists();
        });
        return userRepository.save()
    }

    public Optional<Object> findByLogin(String login) {
        return null;
    }
}
