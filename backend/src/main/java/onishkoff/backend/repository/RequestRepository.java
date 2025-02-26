package onishkoff.backend.repository;


import onishkoff.backend.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByOrganization_Id(Long organizationId);
}
