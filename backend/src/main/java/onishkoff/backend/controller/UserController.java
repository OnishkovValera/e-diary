package onishkoff.backend.controller;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
