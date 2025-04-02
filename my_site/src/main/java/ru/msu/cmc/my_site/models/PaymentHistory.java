package ru.msu.cmc.my_site.models;
import lombok.*;
import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class PaymentHistory implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "employee_id")
    @ToString.Exclude
    @NonNull
    private Employees employeeId;

    @Column(name = "payment", nullable = false)
    @NonNull
    private Integer payment;


    @Column(name = "payment_type", nullable = false)
    @NonNull
    private String paymentType;

    @Column(name = "payment_date", nullable = false)
    @NonNull
    private LocalDate paymentDate;

    public PaymentHistory(Employees employeeId, Integer payment, String paymentType, LocalDate paymentDate) {
        this.employeeId = employeeId;
        this.payment = payment;
        if (paymentType.equals("зарплата") || paymentType.equals("премия")) {
            this.paymentType = paymentType;
        } else {
            throw new IllegalArgumentException(
                    "Некорректный тип выплаты: " + paymentType +
                            ". Допустимые значения: 'зарплата', 'премия'."
            );
        }
        this.paymentDate = paymentDate;
    }
}
