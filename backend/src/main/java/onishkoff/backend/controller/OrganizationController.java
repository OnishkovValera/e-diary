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
//TODO: Add ability to crud organization, approve teacher requests, add teacher to classes, create classes
public class OrganizationController {

    private final OrganizationService organizationService;


    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getOrganizations());
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@RequestBody OrganizationDto organizationDto) {
        System.out.println(organizationDto);
        return ResponseEntity.ok(organizationService.createOrganization(organizationDto));
    }

    @PostMapping("/{id}/addMember/{memberId}")
    public ResponseEntity<Void> addMemberToOrganization(@PathVariable(name = "id") Long organizationId,
                                                        @PathVariable(name = "memberId") Long memberId,
                                                        @RequestParam(name = "status") Role role) {
        return ResponseEntity.ok(organizationService.addMemberToOrganization(organizationId, memberId, role));
    }

    @DeleteMapping("/deleteMember/{memberInOrganizationId}")
    public ResponseEntity<Void> deleteMemberToOrganization(@PathVariable(name = "memberInOrganizationId") Long memberInOrganizationId) {
        return ResponseEntity.ok(organizationService.deleteMemberToOrganization(memberInOrganizationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable(name = "id") Long organizationId) {
        return ResponseEntity.ok(organizationService.deleteOrganization(organizationId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@PathVariable(name = "id") Long organizationId, @RequestParam(name = "name") String name){
        return ResponseEntity.ok(organizationService.updateOrganization(organizationId, name));
    }


}
