package onishkoff.backend.utils;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.exception.NoSuchCourse;
import onishkoff.backend.exception.NoSuchOrganization;
import onishkoff.backend.model.Course;
import onishkoff.backend.model.MembersInOrganization;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.User;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.repository.CourseRepository;
import onishkoff.backend.repository.OrganizationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RightsChecker {


    private final OrganizationRepository organizationRepository;
    private final SecurityUtil securityUtil;
    private final CourseRepository courseRepository;

    public Boolean getCheckAdminRightsOnOrganization(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(NoSuchOrganization::new);
        List<MembersInOrganization> list =  organization.getMembersOrganization();
        for ( MembersInOrganization membersInOrganization : list ) {
            if (membersInOrganization.getRoleInOrganization().equals(Role.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public Boolean getCheckRightsonAdminAndTeacher(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(NoSuchCourse::new);
        User user = securityUtil.getUserFromContext();
        if(getCheckAdminRightsOnOrganization(course.getOrganization().getId())){
            return true;
        }
        return course.getTeacher().equals(user);
    }
}
