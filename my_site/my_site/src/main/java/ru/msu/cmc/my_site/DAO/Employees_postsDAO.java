package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Employees_posts;

import java.util.List;

public interface Employees_postsDAO extends CommonDAO<Employees_posts, Long> {
    List<Employees_posts> getByEmployeeId(Long employeeId);
}