package ru.msu.cmc.my_site.models;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Projects implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projects_id", nullable = false)
    private Long id;

    @Column(name = "projects_name", nullable = false)
    @NonNull
    private String projectName;

    @Column(name = "start_date", nullable = false)
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    @NonNull
    private String status;

    public Projects(String projectName, LocalDate startDate, String status) {
        this.projectName = projectName;
        this.startDate = startDate;
        if (status.equalsIgnoreCase("закончен") || status.equals("не начался") || status.equals("в процессе") || status.equals("закрыт")) {
            this.status = status;
        }
    }

    public Projects(String projectName, LocalDate startDate, LocalDate endDate,String status) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        if (status.equalsIgnoreCase("закончен") || status.equals("не начался") || status.equals("в процессе") || status.equals("закрыт")) {
            this.status = status;
        }
    }
}