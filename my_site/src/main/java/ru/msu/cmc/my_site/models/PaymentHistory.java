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

public class PaymentHistory implements CommonEntity<Long> {
    public enum MyType {
        зарплата,
        премия
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    @NonNull
    private Long employeeId;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;


    @Column(name = "payment_type", nullable = false)
    @NonNull
    private MyType paymentType;

    @Column(name = "payment_date")
    private Date paymentDate;
}
