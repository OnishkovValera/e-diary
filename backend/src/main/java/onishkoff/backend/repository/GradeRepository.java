package onishkoff.backend.repository;

import onishkoff.backend.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<List<Grade>> findGradesByStudentId(Long studentId);
    Optional<List<Grade>> findAllByCourse_Id(Long courseId);

}
