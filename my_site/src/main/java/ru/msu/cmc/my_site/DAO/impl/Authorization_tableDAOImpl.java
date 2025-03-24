package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.Authorization_tableDAO;
import ru.msu.cmc.my_site.models.Authorization_table;

@Repository
public class Authorization_tableDAOImpl
        extends CommonDAOImpl<Authorization_table, Long>
        implements Authorization_tableDAO {

    public Authorization_tableDAOImpl() {
        super(Authorization_table.class);
    }

    @Override
    public Authorization_table getByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            Query<Authorization_table> query = session.createQuery(
                    "FROM Authorization_table WHERE login = :login", Authorization_table.class);
            query.setParameter("login", login);
            return query.uniqueResult();
        }
    }
}