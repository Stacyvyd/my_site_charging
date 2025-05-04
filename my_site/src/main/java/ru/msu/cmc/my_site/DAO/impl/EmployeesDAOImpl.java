package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.EmployeesDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Posts;

import java.util.List;

@Repository
public class EmployeesDAOImpl
        extends CommonDAOImpl<Employees, Long>
        implements EmployeesDAO {

    public EmployeesDAOImpl() {
        super(Employees.class);
    }

    @Override
    public List<Employees> filterEmployees(String namePart, Posts post, Integer experience) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Employees WHERE 1=1");

            if (namePart != null && !namePart.trim().isEmpty()) {
                hql.append(" AND LOWER(name) LIKE LOWER(:namePart)");
            }

            if (post != null) {
                hql.append(" AND postId = :post");
            }

            if (experience != null) {
                hql.append(" AND experience = :experience");
            }

            Query<Employees> query = session.createQuery(hql.toString(), Employees.class);

            if (namePart != null && !namePart.trim().isEmpty()) {
                query.setParameter("namePart", namePart.trim().toLowerCase() + "%");
            }

            if (post != null) {
                query.setParameter("post", post);
            }

            if (experience != null) {
                query.setParameter("experience", experience);
            }

            return query.list();
        }
    }
}