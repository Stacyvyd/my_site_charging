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
public class ProjectsDAOImplTest {
    @Autowired
    private ProjectsDAO projectsDAO;

    @Autowired
    SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        Projects project1 = new Projects("Проект Альфа", LocalDate.of(2023, 01, 01), null, "в процессе");
        projectsDAO.save(project1);
        Projects project2 = new Projects("Проект Бета", LocalDate.of(2022, 11, 10), null, "закончен");
        projectsDAO.save(project2);
    }

    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE projects RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testFilterProjectsRoles() {
        List<Projects> result1 = projectsDAO.filterProjects(null, null);
        assertEquals(2, result1.size());

        List<Projects> result2 = projectsDAO.filterProjects("Проект", null);
        assertEquals(2, result2.size());

        List<Projects> result3 = projectsDAO.filterProjects("", null);
        assertEquals(2, result3.size());

        List<Projects> result4 = projectsDAO.filterProjects(null, "закончен");
        assertEquals(1, result4.size());

        List<Projects> result5 = projectsDAO.filterProjects(null, "");
        assertEquals(2, result5.size());
    }
}
