package ru.msu.cmc.my_site.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.my_site.models.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.my_site.models.*;

        import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class EmployeesPostsDAOImplTest {
    @Autowired
    private EmployeesPostsDAO employeesPostsDAO;

    @Autowired
    private ProjectsDAO projectsDAO;

    @Autowired
    private RolesDAO rolesDAO;

    @Autowired
    private EmployeesDAO employeesDAO;

    @Autowired
    private PostsDAO postsDAO;

    @Autowired
    SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        Posts post1 = new Posts("Менеджер - HR", 60000);
        postsDAO.save(post1);
        Posts post2 = new Posts("Разработчик", 70000);
        postsDAO.save(post2);
        Posts post3 = new Posts("Аналитик", 60000);
        postsDAO.save(post3);
        Posts post4 = new Posts("Тестировщик", 50000);
        postsDAO.save(post4);
        Posts post5 = new Posts("Стажёр", 20000);
        postsDAO.save(post5);

        Employees emp1 = new Employees(1L,"Иван Иванов", "ул. Ленина, 1", post1, 9, LocalDate.of(1990, 5, 15), "Высшее");
        employeesDAO.save(emp1);
        Employees emp2 = new Employees(2L, "Петр Петров", "ул. Советская, 2", post2, 8, LocalDate.of(1992, 7, 20), "Среднее специальное");
        employeesDAO.save(emp2);

        EmployeesPosts ep1 = new EmployeesPosts(emp1, post5, LocalDate.of(2016, 01, 01), LocalDate.of(2018, 07, 01));
        employeesPostsDAO.save(ep1);
        EmployeesPosts ep2 = new EmployeesPosts(emp1, post2, LocalDate.of(2018, 07, 01), LocalDate.of(2020, 04, 02));
        employeesPostsDAO.save(ep2);

        EmployeesPosts ep3 = new EmployeesPosts(emp2, post5, LocalDate.of(2017, 06, 01), LocalDate.of(2019, 07, 01));
        employeesPostsDAO.save(ep3);

    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE Employees_posts RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE employees RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE posts RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterProjectsRoles() {
        List<EmployeesPosts> result1 = employeesPostsDAO.filterPosts(null, null);
        assertEquals(3, result1.size());

        List<EmployeesPosts> result2 = employeesPostsDAO.filterPosts(employeesDAO.getById(1L), null);
        assertEquals(2, result2.size());

        List<EmployeesPosts> result3 = employeesPostsDAO.filterPosts(null, postsDAO.getById(5L));
        assertEquals(2, result3.size());
    }
}
