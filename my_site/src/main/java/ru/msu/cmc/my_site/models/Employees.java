package ru.msu.cmc.my_site.models;
import lombok.*;
import javax.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "experience_payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor

public class Employees implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "birth_date", nullable = false)
    @NonNull
    private Date birthDate;

    @Column(name = "education")
    private String education;
}
