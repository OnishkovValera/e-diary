package onishkoff.backend.repository;

import onishkoff.backend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
