package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Projects;

import java.util.List;

public interface ProjectsDAO extends CommonDAO<Projects, Long> {
    List<Projects> filterProjects(String namePart, String status);
}
