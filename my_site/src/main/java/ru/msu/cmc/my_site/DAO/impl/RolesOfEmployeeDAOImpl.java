package ru.msu.cmc.my_site.DAO.impl;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.RolesOfEmployeeDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Projects;
import ru.msu.cmc.my_site.models.Roles;
import ru.msu.cmc.my_site.models.RolesOfEmployee;

import java.util.List;

@Repository
public class RolesOfEmployeeDAOImpl extends CommonDAOImpl<RolesOfEmployee, Long> implements RolesOfEmployeeDAO {

    public RolesOfEmployeeDAOImpl() {
        super(RolesOfEmployee.class);
    }

    @Override
    public List<RolesOfEmployee> filterRolesHistory(Employees employee, Projects project, Roles role) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM RolesOfEmployee WHERE 1=1");

            if (employee != null) {
                hql.append(" AND employeeId = :employee");
            }
            if (project != null) {
                hql.append(" AND projectId = :project");
            }
            if (role != null) {
                hql.append(" AND roleId = :role");
            }

            Query<RolesOfEmployee> query = session.createQuery(hql.toString(), RolesOfEmployee.class);

            if (employee != null) {
                query.setParameter("employee", employee);
            }
            if (project != null) {
                query.setParameter("project", project);
            }
            if (role != null) {
                query.setParameter("role", role);
            }

            return query.list();
        }
    }
}