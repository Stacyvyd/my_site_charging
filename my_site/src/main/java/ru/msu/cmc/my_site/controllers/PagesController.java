package ru.msu.cmc.my_site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.my_site.DAO.EmployeesDAO;
import ru.msu.cmc.my_site.DAO.PostsDAO;
import ru.msu.cmc.my_site.models.Employees;
import ru.msu.cmc.my_site.models.Posts;

import java.util.List;

import java.security.Principal;

@Controller
public class PagesController {

    @Autowired
    private PostsDAO postsDAO;


    @Autowired
    private EmployeesDAO employeesDAO;

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






    @Autowired
    public PagesController(EmployeesDAO employeesDAO, PostsDAO postsDAO) {
        this.employeesDAO = employeesDAO;
        this.postsDAO = postsDAO;
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
    public String saveEmployee(@ModelAttribute Employees employee,
                               @RequestParam("postId") Long postId) {
        Posts post = postsDAO.getById(postId);
        if (post == null) {
            throw new RuntimeException("Должность с ID " + postId + " не найдена");
        }

        employee.setPostId(post); // Связали объект
        employeesDAO.save(employee);
        return "redirect:/employees";
    }


}
