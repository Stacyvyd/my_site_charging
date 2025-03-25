package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.msu.cmc.my_site.DAO.RolesDAO;
import ru.msu.cmc.my_site.models.Roles;

import java.util.List;

public class RolesDAOImpl extends CommonDAOImpl<Roles, Long> implements RolesDAO {
    public RolesDAOImpl() {
        super(Roles.class);
    }

    @Override
    public List<Roles> filterRoles(String namePart) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Roles WHERE 1=1");

            if (namePart != null && !namePart.isEmpty()) {
                hql.append(" AND lower(roleName) LIKE lower(:namePart)");
            }
            Query<Roles> query = session.createQuery(hql.toString(), Roles.class);

            if (namePart != null && !namePart.isEmpty()) {
                query.setParameter("namePart", "%" + namePart + "%");
            }

            return query.list();
        }
    }
}
