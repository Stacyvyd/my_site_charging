package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.EmployeesPostsDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.EmployeesPosts;
import ru.msu.cmc.my_site.models.Posts;

import java.util.List;

@Repository
public class EmployeesPostsDAOImpl
        extends CommonDAOImpl<EmployeesPosts, Long>
        implements EmployeesPostsDAO {

    public EmployeesPostsDAOImpl() {
        super(EmployeesPosts.class);
    }

    @Override
    public List<EmployeesPosts> filterPosts(Employees employee, Posts post) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM EmployeesPosts WHERE 1=1");

            if (employee != null) {
                hql.append(" AND employeeId = :employee");
            }

            if (post != null) {
                hql.append(" AND postId = :post");
            }

            Query<EmployeesPosts> query = session.createQuery(hql.toString(), EmployeesPosts.class);

            if (employee != null) {
                query.setParameter("employee", employee);
            }

            if (post != null) {
                query.setParameter("post", post);
            }

            return query.list();
        }
    }
}