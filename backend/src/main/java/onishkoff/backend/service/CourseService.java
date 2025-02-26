package onishkoff.backend.service;


import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.exception.*;
import onishkoff.backend.model.Course;
import onishkoff.backend.model.MembersInOrganization;
import onishkoff.backend.model.User;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.repository.CourseRepository;
import onishkoff.backend.repository.OrganizationMemberRepository;
import onishkoff.backend.repository.OrganizationRepository;
import onishkoff.backend.repository.UserRepository;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMemberService organizationMemberService;
    private final OrganizationMemberRepository organizationMemberRepository;


    public List<CourseDto> findAllByOrganization(Long organizationId) {
        organizationRepository.findById(organizationId).orElseThrow(NoSuchOrganization::new);
        List<Course> courses = courseRepository.findByOrganization_Id(organizationId).orElseThrow(NoSuchCourse::new);
        return courses.stream()
                .map(element -> modelMapper
                        .map(element, CourseDto.class))
                .toList();
    }

    public CourseDto findById(Long courseId) {
        return modelMapper.map(courseRepository.findById(courseId).orElseThrow(NoSuchCourse::new), CourseDto.class);
    }

    public CourseDto createCourse(Long organizationId, CourseDto course) {
        course.setTeacher(modelMapper.map(securityUtil.getUserFromContext(), UserDto.class));
        Course newCourse = modelMapper.map(course, Course.class);
        newCourse.setOrganization(organizationRepository.findById(organizationId).orElseThrow(NoSuchOrganization::new));
        return modelMapper.map(courseRepository.save(newCourse), CourseDto.class);
    }

    public void deleteCourse(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(NoSuchCourse::new);
        courseRepository.deleteById(courseId);
    }

    public void setTeacherToCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchCourse::new);
        System.out.println(teacherId + " " + course.getOrganization().getId());
        MembersInOrganization membersInOrganization = organizationMemberRepository.findByMember_IdAndOrganization_Id(teacherId, course.getOrganization().getId()).orElseThrow(NoSuchMemberInOrganization::new);
        if (!membersInOrganization.getRoleInOrganization().equals(Role.STUDENT)) {
            course.setTeacher(membersInOrganization.getMember());
            courseRepository.save(course);
        } else {
            throw new StudentOnTeacherException();
        }
    }

    public void deleteTeacherFromCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchCourse::new);
        if (course.getTeacher() != null) {
            course.setTeacher(null);
            courseRepository.save(course);
        } else {
            throw new CourseHasNoTeacherException();
        }

    }

    public List<CourseDto> getAllCoursesByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new)
                .getCourses()
                .stream()
                .map(course -> modelMapper
                        .map(course, CourseDto.class))
                .toList();


    }
}
