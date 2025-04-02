package ru.msu.cmc.my_site.models;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees_posts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor

public class EmployeesPosts implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "employee_id")
    @ToString.Exclude
    @NonNull
    private Employees employeeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "post_id")
    @ToString.Exclude
    @NonNull
    private Posts postId;

    @Column(name = "start_date", nullable = false)
    @NonNull
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public EmployeesPosts(Employees employeeId, Posts postId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.postId = postId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
