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

import ru.msu.cmc.my_site.models.*;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class RolesOfEmployeeDAOImplTest {
    @Autowired
    private RolesOfEmployeeDAO rolesOfEmployeeDAO;

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
        Projects project1 = new Projects("Проект Альфа", LocalDate.of(2023, 01, 01), null, "в процессе");
        projectsDAO.save(project1);
        Projects project2 = new Projects("Проект 2", LocalDate.of(2023, 01, 01), null, "в процессе");
        projectsDAO.save(project2);

        Roles role1 = new Roles("Руководитель");
        rolesDAO.save(role1);
        Roles role2 = new Roles("Разработчик");
        rolesDAO.save(role2);
        Roles role3 = new Roles("Аналитик");
        rolesDAO.save(role3);
        Roles role4 = new Roles("Тестировщик");
        rolesDAO.save(role4);

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

        RolesOfEmployee emploeerole1 = new RolesOfEmployee(1L, emp1, project1, role1, LocalDate.of(1990, 5, 15), LocalDate.of(1990, 5, 15));
        rolesOfEmployeeDAO.save(emploeerole1);
        RolesOfEmployee emploeerole2 = new RolesOfEmployee(2L, emp1, project2, role1, LocalDate.of(1990, 5, 15), LocalDate.of(1990, 5, 15));
        rolesOfEmployeeDAO.save(emploeerole2);
        RolesOfEmployee emploeerole3 = new RolesOfEmployee(3L, emp2, project1, role2, LocalDate.of(1990, 5, 15), LocalDate.of(1990, 5, 15));
        rolesOfEmployeeDAO.save(emploeerole3);
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE roles_of_employee RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE employees RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE posts RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE roles RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE projects RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterProjectsRoles() {
        List<RolesOfEmployee> result1 = rolesOfEmployeeDAO.filterRolesHistory(null, null, null);
        assertEquals(3, result1.size());

        List<RolesOfEmployee> result2 = rolesOfEmployeeDAO.filterRolesHistory(employeesDAO.getById(1L), null, null);
        assertEquals(2, result2.size());

        List<RolesOfEmployee> result3 = rolesOfEmployeeDAO.filterRolesHistory(null, null, rolesDAO.getById(1L));
        assertEquals(2, result3.size());

        List<RolesOfEmployee> result4 = rolesOfEmployeeDAO.filterRolesHistory(null, projectsDAO.getById(2L), null);
        assertEquals(1, result4.size());
    }
}
