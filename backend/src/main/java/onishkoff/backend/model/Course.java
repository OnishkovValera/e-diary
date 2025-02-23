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
@Table(name = "course")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {

    @Id
    @Column(name = "course_id")
    Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;

    @Column(name = "name")
    String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    User teacher;


    @NotNull
    @Column(name = "created_at")
    LocalDateTime created_at;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    Set<User> students = new HashSet<>();

}
