package onishkoff.backend.utils;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.model.User;
import onishkoff.backend.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserService userService;

    public User getUserFromContext(){
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByLogin(login);

    }
}
