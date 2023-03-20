package com.project.swp.controller;

import com.project.swp.entity.*;
import com.project.swp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CompanyService companyService;
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private BossService bossService;

    // Customer // ==============================================================================================
    @GetMapping("/customer")
    public String CustomerLoginForm(Model model){
        model.addAttribute("errorNotice", null);
        return "customer/login";
    }
    @PostMapping("/customer")
    public String CustomerLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session){
        Customer customer = customerService.authenticate(username, password);

        if(customer != null){
            session.setAttribute("customer", customer);
            return ("redirect:/home/customer");
        }

        model.addAttribute("errorNotice", "Wrong username or password");
        return "customer/login";
    }


    // Manager // ==============================================================================================
    @GetMapping("/staff")
    public String ManagerLoginForm(Model model){
        model.addAttribute("errorNotice", null);
        return "manager/managerlogin";
    }

    @PostMapping("/staff")
    public String ManagerLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session){
        Staff staff = staffService.authenticate(username, password);

        if(staff != null){
            session.setAttribute("staff", staff);
            Restaurant restaurant = restaurantService.getDetailRes(staff.getRole().getRoleId().getRestaurant().getResID());
            session.setAttribute("restaurant", restaurant);
                return ("redirect:/home/manager");
        }

        model.addAttribute("errorNotice", "Wrong username or password");
        return "manager/managerlogin" ;
    }

    // Administrator // ==============================================================================================

    @GetMapping("/admin")
    public String GetFormLoginAdmin(Model model){
        model.addAttribute("errorNotice", null);
        return "admin/login";
    }

    @PostMapping("/admin")
    public String AdminLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session){
        Company company = companyService.authenticate(email, password);
        if(company!=null){
            session.setAttribute("company", company);
            return ("redirect:/home/admin");
        }

        model.addAttribute("errorNotice", "Wrong username or password");
        return "admin/login";
    }

    // Boss // ==============================================================================================
    @GetMapping("/boss")
    public String FormLoginBoss(Model model) {
        return "boss/login";
    }
    @PostMapping("/boss")
    public String BossLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session){
        Boss boss = bossService.Authentication(username, password);
        if(boss!=null){
            session.setAttribute("boss", boss);
            return ("redirect:/home/boss");
        }

        model.addAttribute("errorNotice", "Wrong username or password");
        return "boss/login";
    }

    // Logout // ======================================================================================================
    @GetMapping("/logout")
    public String Logout(HttpSession session){
        session.invalidate();
        return "redirect:/home/default";
    }


}
