package onishkoff.backend.controller.authenticate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping("/login")
    public String authenticate(@RequestParam String username, @RequestParam String password) {

    }

    @PostMapping("/reg")
    public String register(@RequestParam String username, @RequestParam String password) {

    }

}
