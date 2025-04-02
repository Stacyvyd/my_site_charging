package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Roles;
import java.util.List;

@Repository
public interface RolesDAO extends CommonDAO<Roles, Long> {
    List<Roles> filterRoles(String namePart);
}
