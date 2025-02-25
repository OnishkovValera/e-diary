package onishkoff.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import onishkoff.backend.model.enums.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_members")
@Data
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
