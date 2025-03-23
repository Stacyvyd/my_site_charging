package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Authorization_table;

public interface Authorization_tableDAO extends CommonDAO<Authorization_table, Long> {
    Authorization_table getByLogin(String login);
}