package onishkoff.backend.service;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.dto.model.UserDto;
import onishkoff.backend.dto.model.course.CourseDto;
import onishkoff.backend.dto.model.organization.OrganizationDto;
import onishkoff.backend.exception.UserNotFoundException;
import onishkoff.backend.exception.auth.UserAlreadyExists;
import onishkoff.backend.model.User;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.repository.CourseRepository;
import onishkoff.backend.repository.OrganizationMemberRepository;
import onishkoff.backend.repository.OrganizationRepository;
import onishkoff.backend.repository.UserRepository;
import onishkoff.backend.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberService organizationMemberService;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    public User registerUser(RegistrationDto registerDto) {
        userRepository.findByLogin(registerDto.getLogin()).ifPresent(user -> {
            throw new UserAlreadyExists();
        });
        return userRepository.save(modelMapper.map(registerDto, User.class));
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }


    public List<CourseDto> getCourses() {
        return securityUtil.getUserFromContext().getCourses()
                .stream()
                .map(user -> modelMapper
                        .map(user, CourseDto.class))
                .toList();
    }


    public List<OrganizationDto> getOrganizations() {
        User user = securityUtil.getUserFromContext();
        return organizationRepository.findAllByOwner(user)
                .stream()
                .map(organization -> modelMapper
                        .map(organization, OrganizationDto.class))
                .toList();

    }

    public List<CourseDto> findAllByTeacher() {
        User user = securityUtil.getUserFromContext();
        return courseRepository.findAllByTeacher(user).orElse(Collections.emptyList()).stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();

    }

    public UserDto updateUser(UserDto userDto) {
        User user = securityUtil.getUserFromContext();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

}
