package onishkoff.backend.controller;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("getAllFromOrganization/{organizationId}")
    public ResponseEntity<List<CourseDto>> getCourseByOrganization(@PathVariable Long organizationId) {
        return ResponseEntity.ok(courseService.findAllByOrganization(organizationId));
    }

    @PostMapping()
    public ResponseEntity<CourseDto> createCourse(@RequestParam(name = "organization") Long id, @RequestBody CourseDto course) {
        return ResponseEntity.ok(courseService.createCourse(id, course));
    }

    @GetMapping("/{courseId}")
    public CourseDto getCourse(@PathVariable(name = "courseId") Long courseId) {
        return courseService.findById(courseId);
    }



}
