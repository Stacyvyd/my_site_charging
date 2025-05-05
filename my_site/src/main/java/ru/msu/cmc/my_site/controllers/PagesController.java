package ru.msu.cmc.my_site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.my_site.DAO.EmployeesDAO;
import ru.msu.cmc.my_site.DAO.EmployeesPostsDAO;
import ru.msu.cmc.my_site.DAO.PostsDAO;
import ru.msu.cmc.my_site.DAO.RolesOfEmployeeDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Posts;

import java.util.List;

import java.security.Principal;

@Controller
public class PagesController {

    private final PostsDAO postsDAO;
    private final EmployeesDAO employeesDAO;
    private final EmployeesPostsDAO employeesPostsDAO;
    private final RolesOfEmployeeDAO rolesOfEmployeeDAO;

    @Autowired
    public PagesController(
            EmployeesDAO employeesDAO,
            PostsDAO postsDAO,
            EmployeesPostsDAO employeesPostsDAO,
            RolesOfEmployeeDAO rolesOfEmployeeDAO
    ) {
        this.employeesDAO = employeesDAO;
        this.postsDAO = postsDAO;
        this.employeesPostsDAO = employeesPostsDAO;
        this.rolesOfEmployeeDAO = rolesOfEmployeeDAO;
    }

    @GetMapping("/")
    public String indexPage(Model model, Principal principal) {
        String username = (principal != null) ? principal.getName() : "гость";
        model.addAttribute("username", username);
        return "index";
    }


    @GetMapping("/projects")
    public String projectsPage() {
        return "projects";
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
