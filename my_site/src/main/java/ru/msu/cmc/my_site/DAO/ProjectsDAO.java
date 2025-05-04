package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Projects;

import java.util.List;


@Repository
public interface ProjectsDAO extends CommonDAO<Projects, Long> {
    List<Projects> filterProjects(String namePart, String status);
}
