package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.ProjectsRoles;
import java.util.List;

public interface ProjectsRolesDAO {
    List<ProjectsRoles> filterProjectsRoles(Long projectId, Long roleId, Long employeeId);

}