package onishkoff.backend.repository;


import onishkoff.backend.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByOrganization_Id(Long organizationId);
    List<Request> findAllByCourse_Id(Long courseId);

}
