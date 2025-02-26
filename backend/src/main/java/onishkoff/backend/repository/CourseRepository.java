package onishkoff.backend.repository;

import onishkoff.backend.model.Course;
import onishkoff.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<List<Course>> findByOrganization_Id(Long organizationId);

    Optional<List<Course>> findAllByTeacher(User teacher);
}
