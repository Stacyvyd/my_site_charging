package ru.msu.cmc.my_site.models;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "experience_payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor

public class Posts implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(name = "post_name", nullable = false)
    @NonNull
    private String postName;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;
}