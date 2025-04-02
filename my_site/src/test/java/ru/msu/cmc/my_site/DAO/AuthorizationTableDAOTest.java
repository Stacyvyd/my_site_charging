package ru.msu.cmc.my_site.DAO;

import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.SessionFactory;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.my_site.models.AuthorizationTable;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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
        assertFalse(all.isEmpty());
    }

    @Test
    public void testSaveCollection () {
        annihilation();
        assertEquals(0, authorizationTableDAO.getAll().size());
        List<AuthorizationTable> all = new ArrayList<AuthorizationTable>();
        AuthorizationTable testEntry1 = new AuthorizationTable("test_user1", "test_pass1");
        AuthorizationTable testEntry2 = new AuthorizationTable("test_user2", "test_pass2");
        AuthorizationTable testEntry3 = new AuthorizationTable("test_user3", "test_pass3");
        AuthorizationTable testEntry4 = new AuthorizationTable("test_user4", "test_pass4");
        AuthorizationTable testEntry5 = new AuthorizationTable("test_user5", "test_pass5");
        all.add(testEntry1);
        all.add(testEntry2);
        all.add(testEntry3);
        all.add(testEntry4);
        all.add(testEntry5);
        authorizationTableDAO.saveCollection(all);
        assertEquals(5, authorizationTableDAO.getAll().size());
    }

    @Test
    public void testUpdate() {
        AuthorizationTable entity = authorizationTableDAO.getById(1L);
        String login = entity.getLogin();
        entity.setLogin("new_login");
        authorizationTableDAO.update(entity);
        assertNotSame(login, entity.getLogin());
    }

    @Test
    public void testDelete() {
        AuthorizationTable entity = authorizationTableDAO.getById(1L);
        assertNotNull(entity);
        authorizationTableDAO.delete(entity);
        assertNull(authorizationTableDAO.getById(1L));
    }

    @Test
    public void testDeleteById() {
        AuthorizationTable entity = authorizationTableDAO.getById(1L);
        assertNotNull(entity);
        authorizationTableDAO.deleteById(1L);
        assertNull(authorizationTableDAO.getById(1L));
    }
}