package ru.msu.cmc.my_site.DAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.PaymentHistory;

import java.time.LocalDate;
import java.util.List;
public interface PaymentHistoryDAO extends CommonDAO<PaymentHistory, Long> {
    List<PaymentHistory> filterPayments(
            Employees employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String paymentType
    );
}
