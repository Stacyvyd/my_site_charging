package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.ProjectsRolesDAO;
import ru.msu.cmc.my_site.models.ProjectsRoles;

import java.util.List;

@Repository
public class ProjectsRolesDAOImpl extends CommonDAOImpl<ProjectsRoles, Long> implements ProjectsRolesDAO {

    public ProjectsRolesDAOImpl() {
        super(ProjectsRoles.class);
    }

    @Override
    public List<ProjectsRoles> filterProjectsRoles(Long projectId, Long roleId, Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Projects_roles WHERE 1=1");

            if (projectId != null) hql.append(" AND projects_id = :projectId");
            if (roleId != null) hql.append(" AND role_id = :roleId");
            if (employeeId != null) hql.append(" AND employee_id = :employeeId");

            Query<ProjectsRoles> query = session.createQuery(hql.toString(), ProjectsRoles.class);

            if (projectId != null) query.setParameter("projectId", projectId);
            if (roleId != null) query.setParameter("roleId", roleId);
            if (employeeId != null) query.setParameter("employeeId", employeeId);

            return query.list();
        }
    }
}