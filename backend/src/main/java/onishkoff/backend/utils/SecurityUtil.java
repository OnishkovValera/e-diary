package onishkoff.backend.utils;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.exception.UserNotFoundException;
import onishkoff.backend.model.User;
import onishkoff.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;

    public User getUserFromContext(){
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findByLogin(login);

    }
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }
}
