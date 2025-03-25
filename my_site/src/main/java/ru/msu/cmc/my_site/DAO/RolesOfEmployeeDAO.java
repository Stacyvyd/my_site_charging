package ru.msu.cmc.my_site.DAO;
import ru.msu.cmc.my_site.models.RolesOfEmployee;
import java.util.List;

public interface RolesOfEmployeeDAO extends CommonDAO<RolesOfEmployee, Long> {
    List<RolesOfEmployee> filterRolesHistory(Long employeeId, Long projectId, Long roleId);
}