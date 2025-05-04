package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.EmployeesPosts;
import ru.msu.cmc.my_site.models.Posts;

import java.util.List;


@Repository
public interface EmployeesPostsDAO extends CommonDAO<EmployeesPosts, Long> {
    List<EmployeesPosts> filterPosts(Employees employeeId, Posts postId);
}