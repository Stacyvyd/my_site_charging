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
import ru.msu.cmc.my_site.models.Roles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class RolesDAOImplTest {
    @Autowired
    private RolesDAO rolesDAO;

    @Autowired
    SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        Roles roles1 = new Roles("Руководитель");
        rolesDAO.save(roles1);
        Roles roles2 = new Roles("Аналитик");
        rolesDAO.save(roles2);
        Roles roles3 = new Roles("Разработчик");
        rolesDAO.save(roles3);
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE roles RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterProjectsRoles() {
        List<Roles> result1 = rolesDAO.filterRoles(null);
        assertEquals(3, result1.size());

        List<Roles> result2 = rolesDAO.filterRoles("Разработчик");
        assertEquals(1, result2.size());

        List<Roles> result3 = rolesDAO.filterRoles("");
        assertEquals(3, result3.size());
    }
}
