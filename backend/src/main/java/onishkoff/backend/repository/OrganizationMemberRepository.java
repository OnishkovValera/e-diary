package onishkoff.backend.repository;

import onishkoff.backend.model.MembersInOrganization;
import onishkoff.backend.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrganizationMemberRepository extends JpaRepository<MembersInOrganization, Long> {
    @Modifying
    @Transactional
    void deleteByMember_IdAndOrganization_Id(Long memberId, Long organizationId);
    Optional<MembersInOrganization> findByMember_IdAndOrganization_Id(Long memberId, Long organizationId);

    List<MembersInOrganization> findAllByMember_IdAndRoleInOrganization(Long memberId, Role roleInOrganization);
}
