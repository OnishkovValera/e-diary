package onishkoff.backend.model;


import jakarta.persistence.*;
import lombok.*;
import onishkoff.backend.model.enums.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "organization")
public class MembersInOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "role_in_org")
    @Enumerated(EnumType.STRING)
    private Role roleInOrganization;


    @Column(name = "joined_at")
    LocalDateTime joinedAt;


    @PrePersist
    private void onCreate() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }
}
