package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.Employees_postsDAO;
import ru.msu.cmc.my_site.models.Employees_posts;

import java.util.List;

@Repository
public class Employees_postsDAOImpl
        extends CommonDAOImpl<Employees_posts, Long>
        implements Employees_postsDAO {

    public Employees_postsDAOImpl() {
        super(Employees_posts.class);
    }

    @Override
    public List<Employees_posts> getByEmployeeId(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            var cb = session.getCriteriaBuilder();
            var query = cb.createQuery(Employees_posts.class);
            var root = query.from(Employees_posts.class);

            query.select(root)
                    .where(cb.equal(root.get("employee_id"), employeeId));

            return session.createQuery(query).getResultList();
        }
    }
}