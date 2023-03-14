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
    private CategoryMenuService categoryMenuService;
    @Autowired
    private  MenuService menuService;

    @Autowired
    private RoleService roleService;

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

    //edit staff
    @GetMapping("/staff/edit/{id}")
    public String updateStaff(@PathVariable("id") Integer id, Model model){
        try{
            Staff staff = staffService.getById(id);
            model.addAttribute("staff", staff);
            Restaurant restaurant = restaurantService.getDetailRes(id);
            model.addAttribute("resID", restaurant);
            return "manager/updateStaff";
        }catch (Exception ex){
            return "redirect:/home/manager";
        }
    }
    @PostMapping("staff/edit")
    public String updateStaffs(@RequestParam(value = "resID") int resID,
                               @RequestParam(value = "empID") int empID,
                               @RequestParam(value = "empName") String empName,
                               @RequestParam(value = "email") String email,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "phoneNumber") String phoneNumber,
                               @RequestParam(value = "picture") String picture,
                               @RequestParam(value = "salary") double salary,
                               @RequestParam(value = "status") String status,
                               @RequestParam(value = "userName") String userName,
                               @RequestParam(value = "roleID") int roleID){
        Restaurant restaurant = restaurantService.getDetailRes(resID);
        Staff staffCheck = staffService.getDetailStaff(empID);
        Role role = roleService.getRoleByRestaurantAndRoleID(resID, roleID);
        staffCheck.setEmpName(empName);
        staffCheck.setUserName(userName);
        staffCheck.setPassword(password);
        staffCheck.setEmail(email);
        staffCheck.setPhoneNumber(phoneNumber);
        staffCheck.setPicture(picture);
        staffCheck.setSalary(salary);
        staffCheck.setStatus(status);
        staffCheck.getRole().getRoleId().setRestaurant(restaurant);
        staffCheck.setRole(role);
        staffService.saveStaff(staffCheck);
        return "redirect:/home/manager";
    }
    // delete staff by id
    @GetMapping("/menu/delete/{id}")
    public String deleteStaffByID(@PathVariable("id") int empID) throws Exception {
        staffService.deleteStaffById(empID);
        return "redirect:/home/manager";
    }

    // Add new food
    @GetMapping("/new/{id}")
    public String newFood(@PathVariable("id") int id,@ModelAttribute("menu") Menu menu, Model model) throws Exception {
        model.addAttribute("menu", menu);
        Restaurant restaurant = restaurantService.getDetailRes(id);
        model.addAttribute("detail", restaurant);
        return "manager/addNewFood";
    }
    @PostMapping("/new/{id}")
    public String addNewFood(@PathVariable("id") int id, @RequestParam(value = "cateID") int cateID, @ModelAttribute("menu") Menu menu, Model model){
        Restaurant restaurant = restaurantService.getDetailRes(id);
        CategoryMenu categoryMenu = categoryMenuService.getCategoryMenuByCateID(cateID);
        menu.setStatusFood(true);
        menu.setCategoryMenu(categoryMenu);
        menu.setRestaurant(restaurant);
        menuService.save(menu);
        return "redirect:/home/manager";
    }

    // update food
    @GetMapping("/menu/edit/{id}")
    public String getDetailFood(@PathVariable("id") int id, Model model) throws Exception {
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);
        return "manager/updateMenu";
    }

    @PostMapping("/menu/edit")
    public String updateFood(@RequestParam("foodID") int foodID,
                             @RequestParam("resID") int resID,
                             @RequestParam("foodName") String foodName,
                             @RequestParam("descriptionFood") String descriptionFood,
                             @RequestParam("picture") String picture,
                             @RequestParam("price") double price,
                             @RequestParam("cateID") int cateID) throws Exception {
        Menu menuCheck = menuService.getById(foodID);
        Restaurant restaurant = restaurantService.getDetailRes(resID);
        CategoryMenu categoryMenu = categoryMenuService.getCategoryMenuByCateID(cateID);
        menuCheck.setFoodName(foodName);
        menuCheck.setDescriptionFood(descriptionFood);
        menuCheck.setPicture(picture);
        menuCheck.setPrice(price);
        menuCheck.setCategoryMenu(categoryMenu);
        menuCheck.setRestaurant(restaurant);
        menuService.save(menuCheck);
        return "redirect:/home/manager";
    }

    // delete food by foodId
    @GetMapping("/menu/delete/{id}")
    public String deleteFoodById(@PathVariable("id") int foodID) throws Exception {

        menuService.deleteById(foodID);

        return "redirect:/home/manager";
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
