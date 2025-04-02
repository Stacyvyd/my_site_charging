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

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ProjectsRolesDAOTest {
    @Autowired
    private ProjectsRolesDAO projectsRolesDAO;

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
        Employees emp3 = new Employees(3L, "Сергей Сергеев", "ул. Победы, 3", post3, 8, LocalDate.of(1988, 3, 10), "Высшее");
        employeesDAO.save(emp3);
        Employees emp4 = new Employees(4L, "Анна Смирнова", "ул. Мира, 4", post4, 2, LocalDate.of(1965, 12, 15), "Высшее");
        employeesDAO.save(emp4);
        Employees emp5 = new Employees(5L, "Елизаветта Снигирева", "ул. Ленина, 4", post5, 0, LocalDate.of(2000, 10, 23), "Два высших");
        employeesDAO.save(emp5);
        Employees emp6 = new Employees(6L, "Анастасия Нелюбина", "ул. Сокола, 4", post2, 1, LocalDate.of(2001, 01, 12), "Высшее");
        employeesDAO.save(emp6);

        ProjectsRoles projectsrole1 = new ProjectsRoles(project1, role1, emp2, 30000);
        projectsRolesDAO.save(projectsrole1);
        ProjectsRoles projectsrole2 = new ProjectsRoles(project1, role2, emp2, 20000);
        projectsRolesDAO.save(projectsrole2);
        ProjectsRoles projectsrole3 = new ProjectsRoles(project1, role3, emp6, 20000);
        projectsRolesDAO.save(projectsrole3);
        ProjectsRoles projectsrole4 = new ProjectsRoles(project1, role4, emp4, 15000);
        projectsRolesDAO.save(projectsrole4);
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE projects_roles RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE employees RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE posts RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE roles RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE projects RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterProjectsRoles() {
        List<ProjectsRoles> result1 = projectsRolesDAO.filterProjectsRoles(null, null, null);
        assertEquals(4, result1.size());

        List<ProjectsRoles> result2 = projectsRolesDAO.filterProjectsRoles(projectsDAO.getById(1L), null, null);
        assertEquals(4, result2.size());

        List<ProjectsRoles> result3 = projectsRolesDAO.filterProjectsRoles(null, rolesDAO.getById(2L), null);
        assertEquals(1, result3.size());

        List<ProjectsRoles> result4 = projectsRolesDAO.filterProjectsRoles(null, null, employeesDAO.getById(2L));
        assertEquals(2, result4.size());
    }
}
