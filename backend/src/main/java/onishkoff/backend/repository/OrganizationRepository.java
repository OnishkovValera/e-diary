package onishkoff.backend.repository;

import jakarta.validation.constraints.NotNull;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findAllByOwner(@NotNull User owner);
}
