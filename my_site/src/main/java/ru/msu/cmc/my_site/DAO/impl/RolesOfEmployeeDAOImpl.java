package ru.msu.cmc.my_site.DAO.impl;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.RolesOfEmployeeDAO;
import ru.msu.cmc.my_site.models.RolesOfEmployee;

import java.util.List;

@Repository
public class RolesOfEmployeeDAOImpl extends CommonDAOImpl<RolesOfEmployee, Long> implements RolesOfEmployeeDAO {

    public RolesOfEmployeeDAOImpl() {
        super(RolesOfEmployee.class);
    }

    @Override
    public List<RolesOfEmployee> filterRolesHistory(Long employeeId, Long projectId, Long roleId) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Roles_of_employee WHERE 1=1");

            if (employeeId != null) hql.append(" AND employee_id = :employeeId");
            if (projectId != null) hql.append(" AND projects_id = :projectId");
            if (roleId != null) hql.append(" AND role_id = :roleId");

            Query<RolesOfEmployee> query = session.createQuery(hql.toString(), RolesOfEmployee.class);

            if (employeeId != null) query.setParameter("employeeId", employeeId);
            if (projectId != null) query.setParameter("projectId", projectId);
            if (roleId != null) query.setParameter("roleId", roleId);

            return query.list();
        }
    }
}