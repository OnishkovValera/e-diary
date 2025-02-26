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
public class MembersInOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "role_in_org")
    @Enumerated(EnumType.STRING)
    private Role roleInOrganization;


    @Column(name = "joined_at")
    LocalDateTime joinedAt;
}
