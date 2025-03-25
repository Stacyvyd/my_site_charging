package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Roles;
import java.util.List;

public interface RolesDAO extends CommonDAO<Roles, Long> {
    List<Roles> filterRoles(String namePart);
}
