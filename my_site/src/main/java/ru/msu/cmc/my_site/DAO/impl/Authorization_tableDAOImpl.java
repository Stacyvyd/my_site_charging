package ru.msu.cmc.my_site.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.AuthorizationTableDAO;
import ru.msu.cmc.my_site.models.AuthorizationTable;

@Repository
public class Authorization_tableDAOImpl
        extends CommonDAOImpl<AuthorizationTable, Long>
        implements AuthorizationTableDAO {

    public Authorization_tableDAOImpl() {
        super(AuthorizationTable.class);
    }

    @Override
    public AuthorizationTable getByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            Query<AuthorizationTable> query = session.createQuery(
                    "FROM AuthorizationTable WHERE login = :login", AuthorizationTable.class);
            query.setParameter("login", login);
            return query.uniqueResult();
        }
    }
}