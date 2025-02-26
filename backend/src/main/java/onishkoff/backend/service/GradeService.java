package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.grade.GradeDto;
import onishkoff.backend.exception.NoSuchCourse;
import onishkoff.backend.exception.NoSuchGrade;
import onishkoff.backend.exception.NoSuchStudentException;
import onishkoff.backend.exception.UserNotFoundException;
import onishkoff.backend.model.Course;
import onishkoff.backend.model.Grade;
import onishkoff.backend.model.User;
import onishkoff.backend.repository.CourseRepository;
import onishkoff.backend.repository.GradeRepository;
import onishkoff.backend.repository.UserRepository;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SecurityUtil securityUtil;

    public void addGrade(GradeDto grade) {
        Grade newGrade = modelMapper.map(grade, Grade.class);
        User teacher = securityUtil.getUserFromContext();
        User student = userRepository.findById(grade.getStudent().getId()).orElseThrow(UserNotFoundException::new);
        Course course = courseRepository.findById(grade.getCourse().getId()).orElseThrow(NoSuchCourse::new);
        newGrade.setStudent(student);
        newGrade.setTeacher(teacher);
        newGrade.setCourse(course);
        gradeRepository.save(newGrade);
    }

    public void updateGrade(GradeDto grade) {
        Grade newGrade = gradeRepository.findById(grade.getId()).orElseThrow(NoSuchGrade::new);
        User teacher = userRepository.findById(grade.getTeacher().getId()).orElseThrow(UserNotFoundException::new);
        User student = userRepository.findById(grade.getStudent().getId()).orElseThrow(UserNotFoundException::new);
        Course course = courseRepository.findById(grade.getCourse().getId()).orElseThrow(NoSuchCourse::new);
        newGrade.setStudent(student);
        newGrade.setTeacher(teacher);
        newGrade.setCourse(course);
        gradeRepository.save(newGrade);

    }

    public List<GradeDto> getAllGrades(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchCourse::new);
        if (course.getStudents().stream().anyMatch(s -> s.getId().equals(studentId))){
            return gradeRepository.findGradesByStudentId(studentId)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(grade -> modelMapper
                            .map(grade, GradeDto.class))
                    .toList();
        }else {
            throw new NoSuchStudentException();
        }

    }
}
