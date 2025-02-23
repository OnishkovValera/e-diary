package onishkoff.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotNull
    @NotBlank
    @Column(name = "first_name")
    String firstName;

    @NotNull
    @NotBlank
    @Column(name = "last_name")
    String lastName;

    @NotNull
    @NotBlank
    @Email
    @Column(name = "email")
    String login;

    @NotNull
    @NotBlank
    @Column(name = "password_hash")
    String password;

    @NotNull
    @NotBlank
    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "created_at")
    LocalDateTime created_at;

    @ManyToMany(mappedBy = "students")
    Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    Set<Organization> organizations = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
