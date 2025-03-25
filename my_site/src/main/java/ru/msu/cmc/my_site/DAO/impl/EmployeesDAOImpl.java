package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.EmployeesDAO;
import ru.msu.cmc.my_site.models.Employees;

import java.util.List;

@Repository
public class EmployeesDAOImpl
        extends CommonDAOImpl<Employees, Long>
        implements EmployeesDAO {

    public EmployeesDAOImpl() {
        super(Employees.class);
    }

    @Override
    public List<Employees> filterEmployees(String namePart, Long postId, Integer experience) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Employees WHERE 1=1");

            if (namePart != null && !namePart.isEmpty()) {
                hql.append(" AND lower(name) LIKE lower(:namePart)");
            }

            if (postId != null) {
                hql.append(" AND post_id = :postId");
            }

            if (experience != null) {
                hql.append(" AND experience = :experience");
            }

            Query<Employees> query = session.createQuery(hql.toString(), Employees.class);

            if (namePart != null && !namePart.isEmpty()) {
                query.setParameter("namePart", "%" + namePart + "%");
            }

            if (postId != null) {
                query.setParameter("postId", postId);
            }

            if (experience != null) {
                query.setParameter("experience", experience);
            }

            return query.list();
        }
    }
}