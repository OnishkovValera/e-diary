package onishkoff.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import onishkoff.backend.model.enums.Role;
import onishkoff.backend.model.enums.Status;

import java.time.LocalDateTime;

@Table(name = "requests")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @NotNull
    @Column(name = "role_in_org")
    @Enumerated(EnumType.STRING)
    Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "processed_at")
    LocalDateTime processedAt;

}
