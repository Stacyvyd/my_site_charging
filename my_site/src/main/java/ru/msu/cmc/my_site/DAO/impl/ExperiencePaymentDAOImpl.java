package ru.msu.cmc.my_site.DAO.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.ExperiencePaymentDAO;
import ru.msu.cmc.my_site.models.ExperiencePayment;

@Repository
public class ExperiencePaymentDAOImpl
        extends CommonDAOImpl<ExperiencePayment, Integer>
        implements ExperiencePaymentDAO {

    public ExperiencePaymentDAOImpl() {
        super(ExperiencePayment.class);
    }
}

