package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.EmployeesPostsDAO;
import ru.msu.cmc.my_site.models.EmployeesPosts;

import java.util.List;

@Repository
public class EmployeesPostsDAOImpl
        extends CommonDAOImpl<EmployeesPosts, Long>
        implements EmployeesPostsDAO {

    public EmployeesPostsDAOImpl() {
        super(EmployeesPosts.class);
    }

    @Override
    public List<EmployeesPosts> filterPosts(Long employeeId, Long postId) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Employees_posts WHERE 1=1");

            if (employeeId != null) hql.append(" AND employee_id = :employeeId");
            if (postId != null) hql.append(" AND post_id = :postId");

            Query<EmployeesPosts> query = session.createQuery(hql.toString(), EmployeesPosts.class);

            if (employeeId != null) query.setParameter("employeeId", employeeId);
            if (postId != null) query.setParameter("postId", postId);

            return query.list();
        }
    }
}