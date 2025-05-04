package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Posts;

import java.util.List;


@Repository
public interface EmployeesDAO extends CommonDAO<Employees, Long> {
    List<Employees> filterEmployees(String namePart, Posts postId, Integer experience);
}