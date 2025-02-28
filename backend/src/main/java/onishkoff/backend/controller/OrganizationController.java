package onishkoff.backend.controller;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;


    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long id) {
        OrganizationDto dto = organizationService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations(@RequestParam(defaultValue = "", name = "query") String filter) {
        return ResponseEntity.ok(organizationService.getOrganizations(filter));
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@RequestBody OrganizationDto organizationDto) {
        OrganizationDto dto = organizationService.createOrganization(organizationDto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/addMember/{memberId}")
    public ResponseEntity<Void> addMemberToOrganization(@PathVariable(name = "id") Long organizationId,
                                                        @PathVariable(name = "memberId") Long memberId,
                                                        @RequestParam(name = "role") Role role) {
        return ResponseEntity.ok(organizationService.addMemberToOrganization(organizationId, memberId, role));
    }

    @DeleteMapping("/deleteMember/{memberInOrganizationId}")
    public ResponseEntity<Void> deleteMemberFromOrganization(@PathVariable(name = "memberInOrganizationId") Long memberInOrganizationId) {
        return ResponseEntity.ok(organizationService.deleteMemberToOrganization(memberInOrganizationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable(name = "id") Long organizationId) {
        return ResponseEntity.ok(organizationService.deleteOrganization(organizationId));
    }

    @DeleteMapping("/{id}/deleteMember/{memberId}")
    public ResponseEntity<Void> deleteMemberToOrganization(@PathVariable(name = "id") Long organizationId,
                                                        @PathVariable(name = "memberId") Long memberId) {
        System.out.println("deleteMemberToOrganization");
        return ResponseEntity.ok(organizationService.deleteMemberFromOrganization(organizationId, memberId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@PathVariable(name = "id") Long organizationId, @RequestParam(name = "name") String name){
        return ResponseEntity.ok(organizationService.updateOrganization(organizationId, name));
    }


}
