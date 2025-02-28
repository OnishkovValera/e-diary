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



    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestParam(name = "organization") Long id, @RequestBody CourseDto course) {
        return ResponseEntity.ok(courseService.createCourse(id, course));
    }

    @GetMapping("/{courseId}")
    public CourseDto getCourse(@PathVariable(name = "courseId") Long courseId) {
        return courseService.findById(courseId);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(name = "courseId") Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/setTeacher/{TeacherId}")
    public ResponseEntity<Void> setTeacherToCourse(@PathVariable(name = "id") Long courseId, @PathVariable(name = "TeacherId") Long teacherId) {
        courseService.setTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deleteTeacher")
    public ResponseEntity<Void> deleteTeacherFromCourse(@PathVariable(name = "id") Long courseId) {
        courseService.deleteTeacherFromCourse(courseId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getAllByUser/{userId}")
    public ResponseEntity<List<CourseDto>> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getAllCoursesByUserId(userId));
    }


    @DeleteMapping("/deleteMember/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") Long courseId) {
        return ResponseEntity.ok(courseService.deleteMemberFromCourse(courseId));
    }

    @DeleteMapping("{courseId}/deleteMember/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "courseId") Long courseId,
                                             @PathVariable(name = "id") Long studentId) {
        return ResponseEntity.ok(courseService.deleteMemberFromCourseById(courseId, studentId));
    }




}
