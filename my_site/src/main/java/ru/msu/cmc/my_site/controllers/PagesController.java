package ru.msu.cmc.my_site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.my_site.DAO.EmployeesDAO;
import ru.msu.cmc.my_site.DAO.EmployeesPostsDAO;
import ru.msu.cmc.my_site.DAO.PostsDAO;
import ru.msu.cmc.my_site.DAO.RolesOfEmployeeDAO;
import ru.msu.cmc.my_site.DAO.impl.ProjectsDAOImpl;
import ru.msu.cmc.my_site.DAO.impl.ProjectsRolesDAOImpl;
import ru.msu.cmc.my_site.DAO.impl.RolesDAOImpl;
import ru.msu.cmc.my_site.models.*;

import java.time.LocalDate;
import java.util.List;

import java.security.Principal;

@Controller
public class PagesController {

    private final PostsDAO postsDAO;
    private final EmployeesDAO employeesDAO;
    private final EmployeesPostsDAO employeesPostsDAO;
    private final RolesOfEmployeeDAO rolesOfEmployeeDAO;
    private final ProjectsDAOImpl projectsDAO;
    private final ProjectsRolesDAOImpl projectsRolesDAO;
    private final RolesDAOImpl rolesDAO;

    @Autowired
    public PagesController(
            EmployeesDAO employeesDAO,
            PostsDAO postsDAO,
            EmployeesPostsDAO employeesPostsDAO,
            RolesOfEmployeeDAO rolesOfEmployeeDAO,
            ProjectsDAOImpl projectsDAO,
            ProjectsRolesDAOImpl projectsRolesDAO,
            RolesDAOImpl rolesDAO

    ) {
        this.employeesDAO = employeesDAO;
        this.postsDAO = postsDAO;
        this.employeesPostsDAO = employeesPostsDAO;
        this.rolesOfEmployeeDAO = rolesOfEmployeeDAO;
        this.projectsDAO = projectsDAO;
        this.projectsRolesDAO = projectsRolesDAO;
        this.rolesDAO = rolesDAO;
    }

    @GetMapping("/")
    public String indexPage(Model model, Principal principal) {
        String username = (principal != null) ? principal.getName() : "гость";
        model.addAttribute("username", username);
        return "index";
    }


    @GetMapping("/projects")
    public String projectsPage(@RequestParam(required = false) String namePart,
                               @RequestParam(required = false) String status,
                               Model model) {
        List<Projects> filteredProjects = projectsDAO.filterProjects(namePart, status);

        model.addAttribute("projects", filteredProjects);
        model.addAttribute("namePart", namePart);
        model.addAttribute("status", status);
        return "projects";
    }

    @GetMapping("/project/{id}")
    public String projectPage(@PathVariable("id") Long projectId, Model model) {
        Projects project = projectsDAO.getById(projectId);
        if (project == null) {
            throw new RuntimeException("Проект с ID " + projectId + " не найден");
        }

        List<ProjectsRoles> rolesList = projectsRolesDAO.filterProjectsRoles(project, null, null);

        model.addAttribute("project", project);
        model.addAttribute("rolesList", rolesList);
        return "projectPage"; // Название html-шаблона
    }



    @GetMapping("/project/{id}/addParticipant")
    public String addParticipantForm(@PathVariable("id") Long projectId, Model model) {
        Projects project = projectsDAO.getById(projectId);
        if (project == null) throw new RuntimeException("Проект не найден");

        model.addAttribute("project", project);
        model.addAttribute("participants", employeesDAO.getAll());
        model.addAttribute("roles", rolesDAO.getAll());
        model.addAttribute("projectsRoles", new ProjectsRoles());
        return "projectsRolesEdit";
    }


    @PostMapping("/project/{id}/addParticipant")
    public String addParticipant(@PathVariable("id") Long projectId,
                                 @RequestParam Long employeeId,
                                 @RequestParam Long roleId,
                                 @RequestParam Integer payment) {

        Projects project = projectsDAO.getById(projectId);
        Employees employee = employeesDAO.getById(employeeId);
        Roles role = rolesDAO.getById(roleId);

        // Сохраняем в projects_roles (как раньше)
        ProjectsRoles entry = new ProjectsRoles(project, role, employee, payment);
        projectsRolesDAO.save(entry);

        // Также создаём запись в roles_of_employee
        RolesOfEmployee roleHistory = new RolesOfEmployee(employee, project, role, LocalDate.now());

        rolesOfEmployeeDAO.save(roleHistory);

        return "redirect:/project/" + projectId;
    }




    // Новый проект (добавление)
    @GetMapping("/project/edit")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new Projects()); // пустой объект
        return "projectEdit"; // имя HTML-шаблона
    }

    // Редактирование существующего проекта
    @GetMapping("/project/{id}/edit")
    public String editProject(@PathVariable("id") Long id, Model model) {
        Projects project = projectsDAO.getById(id);
        if (project == null) {
            throw new RuntimeException("Проект не найден");
        }
        model.addAttribute("project", project);
        return "projectEdit";
    }

    @PostMapping("/project/{id}/deleteParticipant")
    public String deleteParticipant(@PathVariable("id") Long projectId,
                                    @RequestParam Long participantId) {

        ProjectsRoles pr = projectsRolesDAO.getById(participantId);
        projectsRolesDAO.delete(pr);

        // Найти соответствующую активную запись в roles_of_employee
        List<RolesOfEmployee> matchingRoles = rolesOfEmployeeDAO.filterRolesHistory(pr.getEmployeeId(), pr.getProjectId(), pr.getRoleId());
        for (RolesOfEmployee role : matchingRoles) {
            if (role.getEndDate() == null) {
                role.setEndDate(LocalDate.now());
                rolesOfEmployeeDAO.update(role);
            }
        }

        return "redirect:/project/" + projectId;
    }




    @GetMapping("/projects/new")
    public String newProject(Model model) {
        model.addAttribute("project", new Projects());
        return "projectEdit";
    }

    @PostMapping("/projects/save")
    public String saveProject(@ModelAttribute Projects project) {
        if (project.getId() != null) {
            projectsDAO.update(project);
        } else {
            projectsDAO.save(project);
        }
        return "redirect:/projects";
    }





    @GetMapping("/payment-policies")
    public String paymentPoliciesPage() {
        return "payment-policies";
    }

    @GetMapping("/payment-history")
    public String paymentHistoryPage() {
        return "payment-history";
    }









    @GetMapping("/employees")
    public String getEmployees(@RequestParam(required = false) String namePart,
                               @RequestParam(required = false) Long postId,
                               @RequestParam(required = false) Integer experience,
                               Model model) {

        Posts post = null;

        if (postId != null) {
            post = postsDAO.getById(postId);
            if (post == null) {
                model.addAttribute("error", "Должность с ID " + postId + " не найдена");
            }
        }

        List<Employees> filtered = employeesDAO.filterEmployees(namePart, post, experience);

        model.addAttribute("employees", filtered);
        model.addAttribute("namePart", namePart);
        model.addAttribute("postId", postId);
        model.addAttribute("experience", experience);
        model.addAttribute("posts", postsDAO.getAll());

        return "employees";
    }

    @GetMapping("/employee/{id}")
    public String getEmployeePage(@PathVariable Long id, Model model) {
        Employees employee = employeesDAO.getById(id);
        if (employee == null) {
            throw new RuntimeException("Сотрудник с ID " + id + " не найден");
        }
        model.addAttribute("employee", employee);
        model.addAttribute("positionsHistory", employeesPostsDAO.filterPosts(employee, null));
        model.addAttribute("rolesInProjects", rolesOfEmployeeDAO.filterRolesHistory(employee, null, null));
        return "employeePage"; // это будет имя HTML-файла
    }


    @GetMapping("/employee/edit")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employees()); // Пустой объект
        model.addAttribute("posts", postsDAO.getAll()); // Для выбора должности
        return "employeeEdit";
    }

    // Отображение формы редактирования существующего сотрудника
    @GetMapping("/employee/{id}/edit")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Employees employee = employeesDAO.getById(id);
        if (employee == null) {
            throw new RuntimeException("Сотрудник с ID " + id + " не найден");
        }
        model.addAttribute("employee", employee);
        model.addAttribute("posts", postsDAO.getAll());
        return "employeeEdit";
    }

    // Обработка сохранения нового или отредактированного сотрудника
    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute Employees employee) {
        Long postId = employee.getPostId() != null ? employee.getPostId().getId() : null;

        if (postId == null) {
            throw new RuntimeException("Должность не выбрана");
        }

        Posts post = postsDAO.getById(postId);
        employee.setPostId(post);
        if (employee.getId() != null) {
            employeesDAO.update(employee);
        } else {
            employeesDAO.save(employee);
        }
        //System.out.println("Обновление сотрудника");
        return "redirect:/employees";
    }


    @PostMapping("/employee/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Long id) {
        Employees employee = employeesDAO.getById(id);
        if (employee != null) {
            employeesDAO.delete(employee);
        }
        return "redirect:/employees";
    }



}
