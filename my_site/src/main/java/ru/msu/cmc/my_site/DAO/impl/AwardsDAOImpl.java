package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.AwardsDAO;
import ru.msu.cmc.my_site.models.Awards;

import java.util.List;

@Repository
public class AwardsDAOImpl
        extends CommonDAOImpl<Awards, Long>
        implements AwardsDAO {

    public AwardsDAOImpl() {
        super(Awards.class);
    }

    @Override
    public List<Awards> filterAwards(String namePart) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Awards WHERE 1=1");

            if (namePart != null && !namePart.isEmpty()) {
                hql.append(" AND lower(awardName) LIKE lower(:namePart)");
            }
            Query<Awards> query = session.createQuery(hql.toString(), Awards.class);

            if (namePart != null && !namePart.isEmpty()) {
                query.setParameter("namePart", "%" + namePart + "%");
            }

            return query.list();
        }
    }
}