package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.msu.cmc.my_site.DAO.PaymentHistoryDAO;
import ru.msu.cmc.my_site.models.PaymentHistory;

import java.sql.Date;
import java.util.List;

public class PaymentHistoryDAOImpl extends CommonDAOImpl<PaymentHistory, Long>
        implements PaymentHistoryDAO {

    public PaymentHistoryDAOImpl() {
        super(PaymentHistory.class);
    }

    @Override
    public List<PaymentHistory> filterPayments(
            Long employeeId,
            Date startDate,
            Date endDate,
            String paymentType
    ) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Payment_history WHERE employee_id = :id");

            if (startDate != null) {
                hql.append(" AND payment_date >= :startDate");
            }
            if (endDate != null) {
                hql.append(" AND payment_date <= :endDate");
            }
            if (paymentType != null) {
                hql.append(" AND payment_type = :type");
            }

            Query<PaymentHistory> query = session.createQuery(hql.toString(), PaymentHistory.class);
            query.setParameter("id", employeeId);

            if (startDate != null) {
                query.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                query.setParameter("endDate", endDate);
            }
            if (paymentType != null) {
                query.setParameter("type", paymentType);
            }

            return query.list();
        }
    }
}
