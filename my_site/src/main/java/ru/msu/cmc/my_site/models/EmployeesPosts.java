package ru.msu.cmc.my_site.models;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

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

    @Column(name = "employee_id", nullable = false)
    @NonNull
    private Long employeeId;

    @Column(name = "post_id", nullable = false)
    @NonNull
    private Long postId;

    @Column(name = "start_date", nullable = false)
    @NonNull
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
}
