package ru.msu.cmc.my_site.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.my_site.models.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class PaymentHistoryDAOImplTest {
    @Autowired
    private PaymentHistoryDAO paymentHistoryDAO;

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

        Employees emp1 = new Employees(1L,"Иван Иванов", "ул. Ленина, 1", post1, 9, LocalDate.of(1990, 5, 15), "Высшее");
        employeesDAO.save(emp1);
        Employees emp2 = new Employees(2L, "Петр Петров", "ул. Советская, 2", post2, 8, LocalDate.of(1992, 7, 20), "Среднее специальное");
        employeesDAO.save(emp2);

        PaymentHistory ph1 = new PaymentHistory(emp1, 65000, "зарплата", LocalDate.of(1990, 5, 15));
        paymentHistoryDAO.save(ph1);
        PaymentHistory ph2 = new PaymentHistory(emp1, 65000, "зарплата", LocalDate.of(1990, 6, 15));
        paymentHistoryDAO.save(ph2);
        PaymentHistory ph3 = new PaymentHistory(emp2, 65000, "премия", LocalDate.of(1990, 5, 15));
        paymentHistoryDAO.save(ph3);
        PaymentHistory ph4 = new PaymentHistory(emp2, 65000, "зарплата", LocalDate.of(1990, 5, 15));
        paymentHistoryDAO.save(ph4);

    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE payment_history RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE employees RESTART IDENTITY CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE TABLE posts RESTART IDENTITY CASCADE").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterProjectsRoles() {
        List<PaymentHistory> result1 = paymentHistoryDAO.filterPayments(null, null, null, null);
        assertEquals(4, result1.size());

        List<PaymentHistory> result2 = paymentHistoryDAO.filterPayments(employeesDAO.getById(1L), null, null, null);
        assertEquals(2, result2.size());

        List<PaymentHistory> result3 = paymentHistoryDAO.filterPayments(null,  LocalDate.of(1990, 5, 13),  LocalDate.of(1990, 5, 15), null);
        assertEquals(3, result3.size());

        List<PaymentHistory> result4 = paymentHistoryDAO.filterPayments(null, null, null, "зарплата");
        assertEquals(3, result4.size());
    }
}
