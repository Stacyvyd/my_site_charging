package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.msu.cmc.my_site.DAO.ProjectsDAO;
import ru.msu.cmc.my_site.models.Projects;

import java.util.List;

public class ProjectsDAOImpl extends CommonDAOImpl<Projects, Long>
        implements ProjectsDAO {
    public ProjectsDAOImpl() {
        super(Projects.class);
    }

    @Override
    public List<Projects> filterProjects(String namePart, String status) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Projects WHERE 1=1");

            if (namePart != null && !namePart.isEmpty()) {
                hql.append(" AND lower(projectName) LIKE lower(:namePart)");
            }

            if (status != null && !status.isEmpty()) {
                hql.append(" AND lower(status) LIKE lower(:status)");
            }

            Query<Projects> query = session.createQuery(hql.toString(), Projects.class);

            if (namePart != null && !namePart.isEmpty()) {
                query.setParameter("namePart", "%" + namePart + "%");
            }

            if (status != null && !status.isEmpty()) {
                query.setParameter("status", "%" + status + "%");
            }

            return query.list();
        }
        }
}
