package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Experience_payment;

public interface Experience_paymentDAO extends CommonDAO<Experience_payment, Integer> {
    Experience_payment getByExperience(Integer experience);
}