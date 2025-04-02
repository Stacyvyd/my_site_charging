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
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Posts;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class EmployeesDAOTest {
    @Autowired
    private EmployeesDAO employeesDAO;

    @Autowired
    private PostsDAO postsDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testFilterEmployees() {
        List<Employees> result1 = employeesDAO.filterEmployees(null, null, null);
        assertEquals(6, result1.size());

        List<Employees> result2 = employeesDAO.filterEmployees("", null, null);
        assertEquals(6, result2.size());

        List<Employees> result3 = employeesDAO.filterEmployees("Иван Иванов", null, null);
        assertEquals(1, result3.size());

        List<Employees> result4 = employeesDAO.filterEmployees(null, postsDAO.getById(2L), null);
        assertEquals(2, result4.size());

        List<Employees> result5 = employeesDAO.filterEmployees(null, null, 8);
        assertEquals(2, result5.size());
    }

    @BeforeEach
    public void setUp() {
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
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE employees RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE posts RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
