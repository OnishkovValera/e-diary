package onishkoff.backend.controller;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getCourses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.ok(userService.getCourses());
    }

    @GetMapping("/getOrganizations")
    public ResponseEntity<List<OrganizationDto>> getOrganizations() {
        return ResponseEntity.ok(userService.getOrganizations());
    }

    @GetMapping("/getTeachingCourse")
    public ResponseEntity<List<CourseDto>> getByTeacher() {
        return ResponseEntity.ok(userService.findAllByTeacher());
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

}
