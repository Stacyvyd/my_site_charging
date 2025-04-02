package ru.msu.cmc.my_site.DAO;

import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.SessionFactory;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.my_site.models.Awards;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;




@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class AwardsDAOTest {
    @Autowired
    private AwardsDAO AwardsDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        Awards award1 = new Awards("Лучший сотрудник", 10000, "Отличная работа за год");
        AwardsDAO.save(award1);
        Awards award2 = new Awards("Лучший новичок", 5000, "Выдающиеся результаты за первые месяцы");
        AwardsDAO.save(award2);
        Awards award3 = new Awards("За вклад в проект", 7000, "Значительный вклад в проект компании");;
        AwardsDAO.save(award3);
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE awards RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterAwards() {
        List<Awards> result1 = AwardsDAO.filterAwards(null);
        assertEquals(3, result1.size());

        List<Awards> result2 = AwardsDAO.filterAwards("");
        assertEquals(3, result2.size());

        List<Awards> result3 = AwardsDAO.filterAwards("Лучший новичок");
        assertEquals(1, result3.size());

        List<Awards> result4 = AwardsDAO.filterAwards("Лучший");
        assertEquals(2, result4.size());
    }

}