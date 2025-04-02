package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.PaymentHistoryDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.PaymentHistory;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PaymentHistoryDAOImpl extends CommonDAOImpl<PaymentHistory, Long>
        implements PaymentHistoryDAO {

    public PaymentHistoryDAOImpl() {
        super(PaymentHistory.class);
    }

    @Override
    public List<PaymentHistory> filterPayments(
            Employees employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String paymentType
    ) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM PaymentHistory WHERE 1 = 1");

            if (employeeId != null) {
                hql.append(" AND employeeId = :employee");
            }
            if (startDate != null) {
                hql.append(" AND paymentDate >= :startDate");
            }
            if (endDate != null) {
                hql.append(" AND paymentDate <= :endDate");
            }
            if (paymentType != null) {
                hql.append(" AND paymentType = :paymentType");
            }

            Query<PaymentHistory> query = session.createQuery(hql.toString(), PaymentHistory.class);

            if (employeeId != null) {
                query.setParameter("employee", employeeId);
            }
            if (startDate != null) {
                query.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                query.setParameter("endDate", endDate);
            }
            if (paymentType != null) {
                query.setParameter("paymentType", paymentType);
            }

            return query.list();
        }
    }
}
