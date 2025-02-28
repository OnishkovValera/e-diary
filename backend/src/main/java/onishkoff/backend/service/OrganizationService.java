package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.organization.MemberDto;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.exception.NoSuchOrganization;
import onishkoff.backend.model.MembersInOrganization;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.repository.OrganizationMemberRepository;
import onishkoff.backend.repository.OrganizationRepository;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final ModelMapper modelMapper;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberService organizationMemberService;
    private final SecurityUtil securityUtil;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Retryable(value = {Exception.class},
            backoff = @Backoff(delay = 1000))
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public OrganizationDto createOrganization(OrganizationDto organization) {
        organization.setOwner(modelMapper.map(securityUtil.getUserFromContext(), UserDto.class));
        OrganizationDto newOrganization = modelMapper.map(organizationRepository.save(modelMapper.map(organization, Organization.class)), OrganizationDto.class);
        organizationMemberService.addMemberToOrganization(newOrganization.getId(), securityUtil.getUserFromContext().getId(), Role.ADMIN);
        List<MembersInOrganization> members = organizationMemberService.getMembersInOrganization(newOrganization.getId());
        newOrganization.setMemberOrganizations(members.stream().map(membersInOrganization -> modelMapper.map(membersInOrganization, MemberDto.class)).toList());
        return newOrganization;
    }

    public List<OrganizationDto> getOrganizations(String name) {
        Specification<Organization> spec = (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) {
                return cb.conjunction();
            }
            String searchPattern = "%" + name.toLowerCase() + "%";
            return cb.like(
                    cb.lower(root.get("name")),
                    searchPattern
            );
        };
        return organizationRepository.findAll(spec).stream()
                .map(organization -> {
                    OrganizationDto dto = modelMapper.map(organization, OrganizationDto.class);
                    List<MembersInOrganization> members = organizationMemberService.getMembersInOrganization(organization.getId());
                    dto.setMemberOrganizations(members.stream().map(membersInOrganization -> modelMapper.map(membersInOrganization, MemberDto.class)).toList());
                    return dto;
                }).toList();
    }

    public Void addMemberToOrganization(Long organizationId, Long memberId, Role role) {
        organizationMemberService.addMemberToOrganization(organizationId, memberId, role);
        return null;
    }

    public Void deleteOrganization(Long organizationId) {
        organizationRepository.findById(organizationId).orElseThrow(NoSuchOrganization::new);
        organizationRepository.deleteById(organizationId);
        return null;
    }

    public OrganizationDto updateOrganization(Long id, String name) {
        Organization organizationToUpdate = organizationRepository.findById(id).orElseThrow(NoSuchOrganization::new);
        organizationToUpdate.setName(name);
        return modelMapper.map(organizationRepository.save(organizationToUpdate), OrganizationDto.class);
    }

    public Void deleteMemberToOrganization(Long memberInOrganizationId) {
        MembersInOrganization member = organizationMemberRepository.findById(memberInOrganizationId).orElseThrow(NoSuchOrganization::new);
        System.out.println(member.getMember().getId().equals(member.getOrganization().getOwner().getId()));
        if(member.getMember().getId().equals(member.getOrganization().getOwner().getId())){
            return null;
        }else{
            organizationMemberService.deleteById(memberInOrganizationId);
        }

        return null;
    }

    public Void deleteMemberFromOrganization(Long organizationId, Long memberId) {
        organizationMemberService.deleteByMeberIdAndOrganizationId(memberId, organizationId);
        return null;
    }

    public OrganizationDto findById(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(NoSuchOrganization::new);
        List<MembersInOrganization> members = organizationMemberService.getMembersInOrganization(id);
        OrganizationDto dto = modelMapper.map(organization, OrganizationDto.class);
        dto.setMemberOrganizations(members.stream().map(membersInOrganization -> modelMapper.map(membersInOrganization, MemberDto.class)).toList());
        return dto;


    }
}
