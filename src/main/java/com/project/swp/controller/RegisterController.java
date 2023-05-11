package com.project.swp.controller;

import com.project.swp.entity.Company;
import com.project.swp.entity.Customer;
import com.project.swp.service.CompanyService;
import com.project.swp.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private HomeController homeController;
    @Autowired
    private CompanyService companyService;

    // Register for customer //

    @GetMapping("/customer")
    public String RegisterCusForm(Model model, HttpSession session){
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        model.addAttribute("error", null);
        return "customer/registercustomer";
    }

    @PostMapping("/customer")
    public String RegisterCustomer(@ModelAttribute("customer") Customer customer, Model model, HttpSession session){
        Customer temp = customerService.findByUsername(customer.getUserName());
        if(temp != null){
            model.addAttribute("error", "username already exists");
            return "customer/registercustomer";
        }
        customerService.createCustomer(customer);
        session.setAttribute("customer", customer);
        return  ("redirect:/home/customer");
    }

    // Register for company //
    @GetMapping("/company")
    public String RegisterCompany(Model model, HttpSession session) {
        Company company = new Company();
        model.addAttribute("company", company);
        return "admin/registerCompany";
    }

    @PostMapping("/company")
    public String RegisterCompany(@ModelAttribute("company")Company company, Model model) {
        company.setStatus("WaitResponse");

        companyService.SaveCompany(company);
        return ("redirect:/home/default");
    }
}
