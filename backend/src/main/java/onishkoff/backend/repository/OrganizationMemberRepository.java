package onishkoff.backend.repository;

import onishkoff.backend.model.MembersInOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMemberRepository extends JpaRepository<MembersInOrganization, Long> {
}
