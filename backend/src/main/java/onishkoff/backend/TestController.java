package onishkoff.backend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("testcontroller")
public class TestController {

    @PostMapping("/test_mapping")
    public String test(Integer i) {
        return i + "____";
    }

}
