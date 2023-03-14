package com.project.swp.controller;

import com.project.swp.DTO.RevenueDTO;
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

    @Autowired
    private AdminHomeService adminHomeService;

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
        Company company = companyService.findCompanyById(companyId);

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
        Staff staff = (Staff) session.getAttribute("staff");
        model.addAttribute("staff", staff);
        Restaurant restaurant = managerHomeService.getRestaurantByStaff(staff.getEmpId());
        getDetailRestaurant(model, restaurant);
        return "manager/mnghome";
    }

    @GetMapping("/manager/em/{id}")
    public String DetailEmpManager(@PathVariable int id, Model model){
        Staff staff = staffService.getStaffById(id);
        model.addAttribute("staff", staff);
        return "manager/employeeDetail";
    }

    // Admin Place // ==========================================================================================

    @GetMapping("/admin")
    public String AdminHome(Model model, HttpSession session) {
        Company company = (Company) session.getAttribute("company");
        model.addAttribute("company", company);

        List<Restaurant> listRes = adminHomeService.getAllRestaurantByCompany(company.getCompanyID());
        model.addAttribute("listRes", listRes);
        return "admin/adminHome";
    }

    @GetMapping("/admin/restaurant/{id}")
    public String AdminHomeRestaurant(@PathVariable int id, Model model, HttpSession session) {
        Restaurant restaurant = restaurantService.getDetailRes(id);
        getDetailRestaurant(model, restaurant);

        Company company = (Company) session.getAttribute("company");
        model.addAttribute("company", company);

        List<Revenue> listRevenue = adminHomeService.getRevenueByMonth(restaurant.getResID());
        model.addAttribute("listRevenue", listRevenue);
        return "admin/restaurant";
    }

    private void getDetailRestaurant(Model model, Restaurant restaurant) {
        model.addAttribute("restaurant", restaurant);
        List<Staff> listEmployee = managerHomeService.getStaffByRestaurantID(restaurant.getResID());
        model.addAttribute("listEmployee", listEmployee);
        List<Order> orderList = managerHomeService.getOrderByResID(restaurant.getResID());
        model.addAttribute("listOrder", orderList);
        List<Rate> rateList = managerHomeService.getAllRateByRestaurant(restaurant.getResID());
        model.addAttribute("listRate", rateList);
        List<Menu> menuList = managerHomeService.getMenuByRestaurant(restaurant.getResID());
        model.addAttribute("listMenu", menuList);
    }

    // Boss //=======================================================================================
    @GetMapping("/boss")
    public String BossHome(Model model, HttpSession session) {
        List<Company> listCompany = companyService.getListCompany();
        model.addAttribute("listCompany", listCompany);
        return "boss/home";
    }

    // Default home // ==========================================================================================
    @GetMapping("default")
    public String HomeDefault(){
        return "collection/defaultHome";
    }

}
