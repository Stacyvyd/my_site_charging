package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.EmployeesPosts;

import java.util.List;

public interface EmployeesPostsDAO extends CommonDAO<EmployeesPosts, Long> {
    List<EmployeesPosts> filterPosts(Long employeeId, Long postId);
}