package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.exception.MemberAlreadyInOrganization;
import onishkoff.backend.model.MembersInOrganization;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.User;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.repository.OrganizationMemberRepository;
import onishkoff.backend.repository.OrganizationRepository;
import onishkoff.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationMemberService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    public void addMemberToOrganization(Long organizationId, Long memberId, Role role) {
        User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new RuntimeException("Organization not found"));
        if (organization.getMembersOrganization()
                .stream()
                .noneMatch(member -> member.getMember()
                        .getId()
                        .equals(memberId))) {
            organizationMemberRepository.save(MembersInOrganization.builder()
                    .organization(organization)
                    .member(user)
                    .roleInOrganization(role)
                    .build());
        }else{
            throw new MemberAlreadyInOrganization();
        }
    }

    public void deleteById(Long organizationId) {
        organizationMemberRepository.findById(organizationId).orElseThrow(() -> new RuntimeException("Такого человека нет в этом курсе"));
        organizationRepository.deleteById(organizationId);
    }

    public void deleteByMeberIdAndOrganizationId(Long memberId, Long organizationId) {
        organizationMemberRepository.deleteByMember_IdAndOrganization_Id(memberId, organizationId);

    }

}
