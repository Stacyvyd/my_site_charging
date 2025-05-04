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
import java.time.LocalDate;

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




}
