package onishkoff.backend.service;


import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.exception.NoSuchCourse;
import onishkoff.backend.exception.NoSuchOrganization;
import onishkoff.backend.model.Course;
import onishkoff.backend.repository.CourseRepository;
import onishkoff.backend.repository.OrganizationRepository;
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


    public List<CourseDto> findAllByOrganization(Long organizationId) {
        return courseRepository.findByOrganization_Id(organizationId).stream()
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
}
