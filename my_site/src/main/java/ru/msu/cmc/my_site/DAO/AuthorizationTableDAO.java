package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.AuthorizationTable;


@Repository
public interface AuthorizationTableDAO extends CommonDAO<AuthorizationTable, Long> {
    AuthorizationTable getByLogin(String login);
}