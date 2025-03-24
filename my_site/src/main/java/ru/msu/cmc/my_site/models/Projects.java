package ru.msu.cmc.my_site.models;
import lombok.*;
import javax.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "projects")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Projects implements CommonEntity<Long> {
    public enum ProjectType {
        закончен,
        не_начался,
        в_процессе,
        закрыт
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projects_id", nullable = false)
    private Long id;

    @Column(name = "projects_name", nullable = false)
    @NonNull
    private String projectName;

    @Column(name = "start_date", nullable = false)
    @NonNull
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status", nullable = false)
    @NonNull
    private ProjectType status;
}