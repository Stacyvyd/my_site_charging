package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.Experience_paymentDAO;
import ru.msu.cmc.my_site.models.Experience_payment;

@Repository
public class Experience_paymentDAOImpl
        extends CommonDAOImpl<Experience_payment, Integer>
        implements Experience_paymentDAO {

    public Experience_paymentDAOImpl() {
        super(Experience_payment.class);
    }

    @Override
    public Experience_payment getByExperience(Integer experience) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Experience_payment.class, experience);
        }
    }
}