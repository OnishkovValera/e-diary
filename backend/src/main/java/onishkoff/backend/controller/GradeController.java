package onishkoff.backend.controller;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.grade.GradeDto;
import onishkoff.backend.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<Void> addGrade(@RequestBody GradeDto grade) {
        System.out.println(grade);
        gradeService.addGrade(grade);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateGrade(@RequestBody GradeDto grade) {
        gradeService.updateGrade(grade);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byStudent")
    public ResponseEntity<List<GradeDto>> getAllGrades(@RequestParam(name = "course") Long courseId, @RequestParam(name = "student") Long studentId) {
        return ResponseEntity.ok(gradeService.getAllGrades(courseId, studentId));
    }

    @GetMapping
    public ResponseEntity<List<GradeDto>> getAllGrades(@RequestParam(name = "course") Long courseId) {
        return ResponseEntity.ok(gradeService.getAllGrades(courseId));
    }


}
