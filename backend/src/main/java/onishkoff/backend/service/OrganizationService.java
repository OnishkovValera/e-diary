package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.exception.NoSuchOrganization;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.repository.OrganizationRepository;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final ModelMapper modelMapper;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberService organizationMemberService;
    private final SecurityUtil securityUtil;

    public OrganizationDto createOrganization(OrganizationDto organization) {
        organization.setOwner(modelMapper.map(securityUtil.getUserFromContext(), UserDto.class));
        return modelMapper.map(organizationRepository.save(modelMapper.map(organization, Organization.class)), OrganizationDto.class);
    }

    public List<OrganizationDto> getOrganizations() {
        return organizationRepository.findAll().stream().map(organization -> modelMapper.map(organization, OrganizationDto.class)).toList();
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
        organizationMemberService.deleteById(memberInOrganizationId);
        return null;
    }
}
