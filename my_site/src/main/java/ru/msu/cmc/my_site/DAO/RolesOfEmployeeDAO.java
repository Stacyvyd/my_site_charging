package ru.msu.cmc.my_site.DAO;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Projects;
import ru.msu.cmc.my_site.models.Roles;
import ru.msu.cmc.my_site.models.RolesOfEmployee;
import java.util.List;


@Repository
public interface RolesOfEmployeeDAO extends CommonDAO<RolesOfEmployee, Long> {
    List<RolesOfEmployee> filterRolesHistory(Employees employeeId, Projects projectId, Roles roleId);
}