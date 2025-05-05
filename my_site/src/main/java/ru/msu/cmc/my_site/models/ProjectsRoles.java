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
public class ProjectsRoles implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "projects_id")
    @ToString.Exclude
    @NonNull
    private Projects projectId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "role_id")
    @ToString.Exclude
    @NonNull
    private Roles roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "employee_id")
    @ToString.Exclude
    @NonNull
    private Employees employeeId;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;

}