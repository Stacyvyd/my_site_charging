package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Employees;

import java.util.List;

public interface EmployeesDAO extends CommonDAO<Employees, Long> {
    List<Employees> filterEmployees(String namePart, Long postId, Integer experience);
}