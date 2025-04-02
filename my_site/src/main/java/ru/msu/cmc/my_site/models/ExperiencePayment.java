package ru.msu.cmc.my_site.models;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "experience_payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor

public class ExperiencePayment implements CommonEntity<Integer> {

    @Id
    @Column(name = "experience", nullable = false)
    private Integer id;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;
}