package onishkoff.backend.repository;

import onishkoff.backend.model.MembersInOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface OrganizationMemberRepository extends JpaRepository<MembersInOrganization, Long> {
    @Modifying
    @Transactional
    void deleteByMember_IdAndOrganization_Id(Long memberId, Long organizationId);
}
