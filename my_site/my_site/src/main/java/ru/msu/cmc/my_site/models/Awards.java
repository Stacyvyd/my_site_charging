package ru.msu.cmc.my_site.models;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "awards")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Awards implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "awards_id", nullable = false)
    private Long id;

    @Column(name = "awards_name", nullable = false)
    @NonNull
    private String awardName;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;

    @Column(name = "description")
    private String description;
}
