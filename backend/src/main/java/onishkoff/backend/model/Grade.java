package onishkoff.backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Grade {

    @Id
    @Column(name = "grade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id")
    User student;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    User teacher;

    @NotNull
    @Column(name = "grade")
    Long grade;

    @Column(name = "grade_date")
    LocalDateTime gradeDateTime;


    @Column(name = "comment")
    String comment;


    @PrePersist
    private void onCreate() {
        if (gradeDateTime == null) {
            gradeDateTime = LocalDateTime.now();
        }
    }
}
