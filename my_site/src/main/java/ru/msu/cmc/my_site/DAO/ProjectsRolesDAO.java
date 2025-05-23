package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Projects;
import ru.msu.cmc.my_site.models.ProjectsRoles;
import ru.msu.cmc.my_site.models.Roles;

import java.util.List;


@Repository
public interface ProjectsRolesDAO extends CommonDAO<ProjectsRoles, Long>{
    List<ProjectsRoles> filterProjectsRoles(Projects projectId, Roles roleId, Employees employeeId);
}