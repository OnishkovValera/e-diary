package onishkoff.backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "organization_id")
    Long id;

    @NotNull
    @Column(nullable = false, name = "name")
    String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "organization_members",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    Set<User> members = new HashSet<>();

}
