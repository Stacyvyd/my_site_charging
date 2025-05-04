package ru.msu.cmc.my_site.DAO;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.PaymentHistory;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PaymentHistoryDAO extends CommonDAO<PaymentHistory, Long> {
    List<PaymentHistory> filterPayments(
            Employees employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String paymentType
    );
}
