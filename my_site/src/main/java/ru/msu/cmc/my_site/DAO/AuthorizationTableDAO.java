package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.AuthorizationTable;

public interface AuthorizationTableDAO extends CommonDAO<AuthorizationTable, Long> {
    AuthorizationTable getByLogin(String login);
}