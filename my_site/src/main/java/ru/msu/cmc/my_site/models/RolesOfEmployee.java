package ru.msu.cmc.my_site.models;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "roles_of_employee")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class RolesOfEmployee implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @ToString.Exclude
    @NonNull
    private Employees employeeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projects_id", nullable = false)
    @ToString.Exclude
    @NonNull
    private Projects projectId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    @NonNull
    private Roles roleId;

    @Column(name = "start_date", nullable = false)
    @NonNull
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}