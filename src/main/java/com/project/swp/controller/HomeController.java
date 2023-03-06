package com.project.swp.controller;

import com.project.swp.entity.*;
import com.project.swp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/home")
public class HomeController {
    @Autowired
    private CustomerHomeService customerHomeService;
    @Autowired
    private ManagerHomeService managerHomeService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private StaffService staffService;

    // Customer place // =========================================================================================

    @GetMapping("/customer")
    public String dataHomePage(Model model) {
        List<Restaurant> listHotRestaurant = customerHomeService.getListHotRestaurant();
        model.addAttribute("listHotRestaurant", listHotRestaurant);
        List<String> listCity = customerHomeService.getListCity();
        model.addAttribute("listCity", listCity);
        List<Company> listCompany = customerHomeService.getListCompany();
        model.addAttribute("listCompany", listCompany);
        List<String> listCategoryRes = customerHomeService.getCategoryRes();
        model.addAttribute("listCategoryRes", listCategoryRes);
        List<Menu> listHotFood = customerHomeService.getListHotFood();
        model.addAttribute("listHotFood", listHotFood);
        return "customer/home";
    }

    @PostMapping("/customer/search")
    public String  searchRestaurant(@RequestParam(name = "company", required = false, defaultValue = "0") int companyId,
                                    @RequestParam(name = "city", required = false, defaultValue = "") String city,
                                    @RequestParam(name = "restaurantName", required = false, defaultValue = "") String restaurantName,
                                    @RequestParam(name = "category", required = false, defaultValue = "") String category,
                                    Model model){
        Company company = companyService.findCompanyByName(companyId);

        List<Restaurant> listRestaurant = restaurantService.searchRestaurant(company, city, restaurantName, category);
        model.addAttribute("listRestaurant", listRestaurant);
//        return listRestaurant;
        return "customer/search";
    }

    @GetMapping("/customer/search/{category}")
    public String searchByCate(@PathVariable String category, Model model) {
        List<Restaurant> listRestaurant = restaurantService.searchRestaurant(null, "", "", category);
        model.addAttribute("listRestaurant", listRestaurant);

        return "customer/search";
    }

    // Manager place // =========================================================================================
    @GetMapping("/manager")
    public String ManagerHome(Model model, HttpSession session) {
        Staff staff = (Staff) session.getAttribute("manager");
        model.addAttribute("staff", staff);
        Restaurant restaurant = managerHomeService.getRestaurantByStaff(staff.getEmpId());
        model.addAttribute("restaurant", restaurant);
        List<Staff> listEmployee = managerHomeService.getStaffByRestaurantID(restaurant.getResID());
        model.addAttribute("listEmployee", listEmployee);
        List<Order> orderList = managerHomeService.getOrderByResID(restaurant.getResID());
        model.addAttribute("listOrder", orderList);
        List<Rate> rateList = managerHomeService.getAllRateByRestaurant(restaurant.getResID());
        model.addAttribute("listRate", rateList);
        List<Menu> menuList = managerHomeService.getMenuByRestaurant(restaurant.getResID());
        model.addAttribute("listMenu", menuList);
        return "manager/mnghome";
    }

    @GetMapping("/manager/em/{id}")
    public String DetailEmpManager(@PathVariable int empId, Model model){
        Staff staff = staffService.getStaffById(empId);
        model.addAttribute("staff", staff);
        return "manager/employeeDetail";
    }

    // Default home // ==========================================================================================
    @GetMapping("default")
    public String HomeDefault(){
        return "collection/defaultHome";
    }
}
