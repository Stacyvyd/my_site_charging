package ru.msu.cmc.my_site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.my_site.DAO.EmployeesDAO;
import ru.msu.cmc.my_site.DAO.EmployeesPostsDAO;
import ru.msu.cmc.my_site.DAO.PostsDAO;
import ru.msu.cmc.my_site.DAO.RolesOfEmployeeDAO;
import ru.msu.cmc.my_site.DAO.impl.*;
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
    private final ExperiencePaymentDAOImpl experiencePaymentDAO;
    private final AwardsDAOImpl awardsDAO;
    private final PaymentHistoryDAOImpl paymentHistoryDAO;

    @Autowired
    public PagesController(
            EmployeesDAO employeesDAO,
            PostsDAO postsDAO,
            EmployeesPostsDAO employeesPostsDAO,
            RolesOfEmployeeDAO rolesOfEmployeeDAO,
            ProjectsDAOImpl projectsDAO,
            ProjectsRolesDAOImpl projectsRolesDAO,
            RolesDAOImpl rolesDAO,
            ExperiencePaymentDAOImpl experiencePaymentDAO,
            AwardsDAOImpl awardsDAO,
            PaymentHistoryDAOImpl paymentHistoryDAO

    ) {
        this.employeesDAO = employeesDAO;
        this.postsDAO = postsDAO;
        this.employeesPostsDAO = employeesPostsDAO;
        this.rolesOfEmployeeDAO = rolesOfEmployeeDAO;
        this.projectsDAO = projectsDAO;
        this.projectsRolesDAO = projectsRolesDAO;
        this.rolesDAO = rolesDAO;
        this.experiencePaymentDAO = experiencePaymentDAO;
        this.awardsDAO = awardsDAO;
        this.paymentHistoryDAO = paymentHistoryDAO;
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
        return "paymentNavigation";
    }

    // Отображение страницы выплат по должностям
    @GetMapping("/payment/position")
    public String viewPaymentsByPosition(Model model) {
        model.addAttribute("posts", postsDAO.getAll());
        return "positionPayments";
    }

    @GetMapping("/experience-payments/add")
    public String addExperiencePaymentForm(Model model) {
        model.addAttribute("payment", new ExperiencePayment());  // Создаем новый объект для добавления
        return "experienceAdd";  // Страница для добавления нового стажа
    }

    // Страница редактирования существующей должности
    @GetMapping("/payment/position/edit/{id}")
    public String editPostForm(@PathVariable("id") Long id, Model model) {
        Posts post = postsDAO.getById(id);
        if (post == null) {
            throw new RuntimeException("Должность не найдена");
        }
        model.addAttribute("post", post);
        return "postEdit";
    }

    // Сохранение (новой или отредактированной) должности
    @PostMapping("/payment/position/save")
    public String savePost(@ModelAttribute Posts post) {
        if (post.getId() != null) {
            postsDAO.update(post);
        } else {
            postsDAO.save(post);
        }
        return "redirect:/payment/position";
    }

    // Удаление должности (если никто её не использует)
    @PostMapping("/payment/position/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        Posts post = postsDAO.getById(id);
        if (post == null) {
            throw new RuntimeException("Должность не найдена");
        }

        // Проверка: никто не использует эту должность
        List<Employees> users = employeesDAO.filterEmployees(null, post, null);
        if (!users.isEmpty()) {
            throw new RuntimeException("Нельзя удалить: должность используется сотрудниками");
        }

        postsDAO.delete(post);
        return "redirect:/payment/position";
    }


    @GetMapping("/experience-payments")
    public String experiencePaymentsPage(Model model) {
        model.addAttribute("payments", experiencePaymentDAO.getAll());
        return "experiencePayments";
    }


    @GetMapping("/experience-payments/edit")
    public String addExperienceForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            // Если id есть, то редактируем существующую запись
            ExperiencePayment payment = experiencePaymentDAO.getById(id);
            model.addAttribute("payment", payment);
        } else {
            // Если id нет, то создаем новый объект
            model.addAttribute("payment", new ExperiencePayment());
        }
        return "experienceEdit";
    }


    @GetMapping("/experience-payments/edit/{id}")
    public String editExperiencePayment(@PathVariable("id") Integer id, Model model) {
        ExperiencePayment payment = experiencePaymentDAO.getById(id);
        if (payment == null) {
            throw new RuntimeException("Запись не найдена");
        }
        model.addAttribute("payment", payment);
        return "experienceEdit"; // Страница для редактирования
    }

    @PostMapping("/experience-payments/update")
    public String updateExperiencePayment(@ModelAttribute ExperiencePayment payment) {
        // Сохраняем изменения для существующей записи
        experiencePaymentDAO.update(payment);

        return "redirect:/experience-payments"; // Перенаправляем на страницу с выплатами
    }

   // @PostMapping("/experience-payments/save")
   // public String saveExperiencePayment(@ModelAttribute ExperiencePayment payment) {
   //     experiencePaymentDAO.save(payment);
   //     return "redirect:/experience-payments";  // Перенаправляем на страницу со списком
   // }

    @PostMapping("/experience-payments/delete")
    public String deleteExperience(@RequestParam Integer experience) {
        experiencePaymentDAO.deleteById(experience);
        return "redirect:/experience-payments";
    }





    // Страница с таблицей премий
    @GetMapping("/awards")
    public String getAwards(Model model) {
        model.addAttribute("awards", awardsDAO.getAll());
        return "awards";  // Страница, где выводятся все премии
    }

    // Страница для добавления новой премии
    @GetMapping("/awards/add")
    public String addAwardForm(Model model) {
        model.addAttribute("award", new Awards()); // Пустой объект для добавления
        return "awardEdit"; // Страница для редактирования
    }

    // Страница для редактирования существующей премии
    @GetMapping("/awards/edit/{id}")
    public String editAwardForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("award", awardsDAO.getById(id)); // Загружаем существующую премию
        return "awardEdit"; // Страница для редактирования
    }

    // Сохранение новой или редактирование существующей премии
    @PostMapping("/awards/save")
    public String saveAward(@ModelAttribute Awards award) {
        if (award.getId() == null) {
            awardsDAO.save(award); // Добавляем новую премию
        } else {
            awardsDAO.update(award); // Обновляем существующую
        }
        return "redirect:/awards"; // После сохранения перенаправляем на страницу премий
    }

    // Удаление премии
    @PostMapping("/awards/delete/{id}")
    public String deleteAward(@PathVariable("id") Long id) {
        Awards award = awardsDAO.getById(id);
        if (award != null) {
            awardsDAO.delete(award);
        }
        return "redirect:/awards"; // Перенаправляем обратно на страницу премий
    }








    // Страница с выплатами по фильтрам
    @GetMapping("/payment-history")
    public String getFilteredPayments(@RequestParam(required = false) Long employeeId,
                                      @RequestParam(required = false) String paymentType,
                                      @RequestParam(required = false) String startDate,
                                      @RequestParam(required = false) String endDate,
                                      Model model) {

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;
        Employees employee = (employeeId != null) ? employeesDAO.getById(employeeId) : null;

        model.addAttribute("payments", paymentHistoryDAO.filterPayments(employee, start, end, paymentType));
        model.addAttribute("employees", employeesDAO.getAll()); // Для фильтрации по сотрудникам
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("paymentType", paymentType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "paymentHistoryList"; // Страница списка выплат
    }

    // Страница для добавления новой выплаты
    @GetMapping("/payment-history/add")
    public String addPaymentForm(Model model) {
        model.addAttribute("payment", new PaymentHistory());
        model.addAttribute("employees", employeesDAO.getAll()); // Список сотрудников для выбора
        return "paymentHistoryForm"; // Страница формы добавления
    }

    // Страница для редактирования выплаты
    @GetMapping("/payment-history/edit/{id}")
    public String editPaymentForm(@PathVariable("id") Long id, Model model) {
        PaymentHistory payment = paymentHistoryDAO.getById(id);
        model.addAttribute("payment", payment);
        model.addAttribute("employees", employeesDAO.getAll()); // Список сотрудников для выбора
        return "paymentHistoryForm"; // Страница формы редактирования
    }

    // Сохранение новой выплаты или обновление существующей
    @PostMapping("/payment-history/save")
    public String saveOrUpdatePaymentHistory(@ModelAttribute PaymentHistory payment) {
        // Если это новая запись (id == null), то сохраняем
        if (payment.getId() == null) {
            paymentHistoryDAO.save(payment);
        } else {
            // Если запись существует (id != null), то обновляем
            paymentHistoryDAO.update(payment);
        }
        return "redirect:/payment-history";  // Перенаправляем на страницу со списком
    }

    // Удаление выплаты
    @PostMapping("/payment-history/delete/{id}")
    public String deletePayment(@PathVariable("id") Long id) {
        PaymentHistory payment = paymentHistoryDAO.getById(id);
        if (payment != null) {
            paymentHistoryDAO.delete(payment);
        }
        return "redirect:/payment-history"; // Перенаправляем на страницу списка выплат
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
