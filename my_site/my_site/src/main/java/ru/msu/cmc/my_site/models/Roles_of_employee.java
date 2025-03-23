package ru.msu.cmc.my_site.models;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "roles_of_employee")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Roles_of_employee implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    @NonNull
    private Long employeeId;

    @Column(name = "projects_id", nullable = false)
    @NonNull
    private Long projectId;

    @Column(name = "role_id", nullable = false)
    @NonNull
    private Long roleId;

    @Column(name = "start_date", nullable = false)
    @NonNull
    private java.sql.Date startDate;

    @Column(name = "end_date")
    private java.sql.Date endDate;
}