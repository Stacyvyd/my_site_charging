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

public class Experience_payment implements CommonEntity<Integer> {

    @Id
    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;

    @Override
    public Integer getId() {
        return this.experience;
    }

    @Override
    public void setId(Integer id) {
        this.experience = id;
    }
}