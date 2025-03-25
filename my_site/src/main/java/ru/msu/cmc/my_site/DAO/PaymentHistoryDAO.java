package ru.msu.cmc.my_site.DAO;
import ru.msu.cmc.my_site.models.PaymentHistory;

import java.sql.Date;
import java.util.List;
public interface PaymentHistoryDAO extends CommonDAO<PaymentHistory, Long> {
    List<PaymentHistory> filterPayments(
            Long employeeId,
            Date startDate,
            Date endDate,
            String paymentType
    );
}
