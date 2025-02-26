package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.dto.model.request.RequestDto;
import onishkoff.backend.exception.*;
import onishkoff.backend.model.Course;
import onishkoff.backend.model.Organization;
import onishkoff.backend.model.Request;
import onishkoff.backend.model.User;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.model.enums.Status;
import onishkoff.backend.repository.CourseRepository;
import onishkoff.backend.repository.OrganizationRepository;
import onishkoff.backend.repository.RequestRepository;
import onishkoff.backend.repository.UserRepository;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final OrganizationRepository organizationRepository;
    private final SecurityUtil securityUtil;
    private final OrganizationMemberService organizationMemberService;
    private final CourseService courseService;
    private final UserRepository userRepository;

    public void createRequest(RequestDto request) {
        request.setId(null);
        Organization organization = organizationRepository.findById(request.getOrganization().getId()).orElseThrow(NoSuchOrganization::new);

        if (request.getRole().equals(Role.STUDENT)) {
            if (request.getCourse() == null) throw new StudentNotSelectCourseException();
            Course checkCourse = courseRepository.findById(request.getCourse().getId()).orElseThrow(NoSuchCourse::new);
            if (checkCourse.getOrganization().equals(organization)) {
                request.setCourse(modelMapper.map(organization, CourseDto.class));
            } else {
                throw new NoSuchCourse();
            }
        }
        request.setUser(modelMapper.map(securityUtil.getUserFromContext(), UserDto.class));
        request.setOrganization(modelMapper.map(organization, OrganizationDto.class));
        Request newRequest = modelMapper.map(request, Request.class);
        newRequest.setStatus(Status.PENDING);
        requestRepository.save(newRequest);
    }


    public List<RequestDto> getAllTeacherRequests(Long organizationId) {
        return requestRepository.findByOrganization_Id(organizationId)
                .stream().filter(request -> request.getRole().equals(Role.TEACHER))
                .map(request -> modelMapper
                        .map(request, RequestDto.class))
                .collect(Collectors.toList());

    }

    public List<RequestDto> getAllStudentRequests(Long organizationId) {
        return requestRepository.findByOrganization_Id(organizationId)
                .stream().filter(request -> request.getRole().equals(Role.STUDENT))
                .map(request -> modelMapper
                        .map(request, RequestDto.class))
                .collect(Collectors.toList());
    }

    public List<RequestDto> getAllAdminRequests(Long organizationId) {
        return requestRepository.findByOrganization_Id(organizationId)
                .stream().filter(request -> request.getRole().equals(Role.ADMIN))
                .map(request -> modelMapper
                        .map(request, RequestDto.class))
                .collect(Collectors.toList());

    }

    @Retryable(value = {CannotAcquireLockException.class},
            backoff = @Backoff(delay = 1000))
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void handleRequest(Long id, Status status) {
        if (status.equals(Status.PENDING)) throw new WrongStatusException();
        Request req = requestRepository.findById(id).orElseThrow(NoSuchRequestException::new);
        req.setStatus(status);
        requestRepository.save(req);
        if (status.equals(Status.APPROVED)) {
            organizationMemberService.addMemberToOrganization(req.getOrganization().getId(), req.getUser().getId(), Role.STUDENT);
            if (req.getRole().equals(Role.STUDENT)) {
                Course course = courseRepository.findById(req.getCourse().getId()).orElseThrow(NoSuchCourse::new);
                User user = userRepository.findById(req.getUser().getId()).orElseThrow(UserNotFoundException::new);
                course.getStudents().add(user);
                courseRepository.save(course);
            }

        }

    }
}
