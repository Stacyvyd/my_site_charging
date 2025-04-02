package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.ProjectsRolesDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Projects;
import ru.msu.cmc.my_site.models.ProjectsRoles;
import ru.msu.cmc.my_site.models.Roles;

import java.util.List;

@Repository
public class ProjectsRolesDAOImpl extends CommonDAOImpl<ProjectsRoles, Long> implements ProjectsRolesDAO {

    public ProjectsRolesDAOImpl() {
        super(ProjectsRoles.class);
    }

    @Override
    public List<ProjectsRoles> filterProjectsRoles(Projects project, Roles role, Employees employee) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM ProjectsRoles WHERE 1=1");

            // Добавляем условия фильтрации
            if (project != null) {
                hql.append(" AND projectId = :project");
            }
            if (role != null) {
                hql.append(" AND roleId = :role");
            }
            if (employee != null) {
                hql.append(" AND employeeId = :employee");
            }

            Query<ProjectsRoles> query = session.createQuery(hql.toString(), ProjectsRoles.class);

            // Устанавливаем параметры
            if (project != null) {
                query.setParameter("project", project);
            }
            if (role != null) {
                query.setParameter("role", role);
            }
            if (employee != null) {
                query.setParameter("employee", employee);
            }

            return query.list();
        }
    }
}