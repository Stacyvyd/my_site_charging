package ru.msu.cmc.my_site.DAO;

import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.SessionFactory;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.my_site.DAO.impl.AuthorizationTableDAOImpl;
import ru.msu.cmc.my_site.models.AuthorizationTable;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class AuthorizationTableDAOTest {

    @Autowired
    private AuthorizationTableDAO authorizationTableDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        AuthorizationTable testEntry = new AuthorizationTable();
        testEntry.setLogin("test_user");
        testEntry.setPassword("test_pass");
        authorizationTableDAO.save(testEntry);
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE authorization_table RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testGetById() {
        AuthorizationTable result = authorizationTableDAO.getById(1L);
        assertNotNull(result);
        assertEquals("test_user", result.getLogin());
    }

    @Test
    public void testGetLogin() {
        AuthorizationTable testEntry = new AuthorizationTable();
        testEntry.setLogin("login");
        testEntry.setPassword("password");
        String result = testEntry.getLogin();
        assertEquals("login", result);
    }

    @Test
    public void testGetByLogin() {
        AuthorizationTable result = authorizationTableDAO.getByLogin("test_user");
        assertNotNull(result);
        assertEquals(1L, result.getId());

        result = authorizationTableDAO.getByLogin("fake_user");
        assertNull(result);
    }

    @Test
    public void testGetAll() {
        List<AuthorizationTable> all = (List<AuthorizationTable>) authorizationTableDAO.getAll();
        assertTrue(all.size() > 0);
    }
}