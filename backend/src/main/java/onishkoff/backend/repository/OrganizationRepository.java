package onishkoff.backend.repository;

import jakarta.validation.constraints.NotNull;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {
    List<Organization> findAllByOwner(@NotNull User owner);
}
