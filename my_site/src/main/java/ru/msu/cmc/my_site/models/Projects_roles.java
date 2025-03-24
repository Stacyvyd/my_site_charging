package ru.msu.cmc.my_site.models;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "projects_roles")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Projects_roles implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "projects_id", nullable = false)
    @NonNull
    private Long projectId;

    @Column(name = "role_id", nullable = false)
    @NonNull
    private Long roleId;

    @Column(name = "employee_id", nullable = false)
    @NonNull
    private Long employeeId;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;
}