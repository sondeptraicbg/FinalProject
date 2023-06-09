package com.project.swp.controller;

import com.project.swp.entity.*;
import com.project.swp.service.*;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private CategoryMenuService categoryMenuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RateService rateService;

    @GetMapping("/customer/{id}")
    public String  getDetailRestaurant(@PathVariable Integer id, Model model, HttpSession httpSession) {
        Customer customer = (Customer) httpSession.getAttribute("customer");
        List<Rate> listRateByResId = rateService.getListRateByResId(id);
        model.addAttribute("listRate", listRateByResId);

        List<Order> orderList = orderService.getListOrderByCusId(customer.getCusID());

        if(orderList.isEmpty())
            model.addAttribute("rateBefore", "rateBefore");
        else if (listRateByResId.stream().anyMatch(rate -> rate.getRateId().getCustomer().equals(customer))) {
            model.addAttribute("rateBefore", "rateBefore");
        } else {
            model.addAttribute("rateBefore", "");
        }

        Restaurant restaurant = restaurantService.getDetailRes(id);
        model.addAttribute("detail", restaurant);
        List<Menu> listMenuDetailRes = menuService.getListMenuByResId(id);
        model.addAttribute("listMenuDetailRes", listMenuDetailRes);
        List<CategoryMenu> categoryMenus = categoryMenuService.getListCategory();
        model.addAttribute("listCategoryMenu", categoryMenus);

        Order order = new Order();
        model.addAttribute("order", order);

        return "customer/detailRestaurant";
    }


    @GetMapping("customer/searchMenu/{resId}/{cateId}")
    public String listMenuByCateID(@PathVariable("resId") int resId, @PathVariable("cateId") int cateId, Model model) {
        Restaurant restaurant = restaurantService.getDetailRes(resId);
        model.addAttribute("detail", restaurant);
        List<CategoryMenu> categoryMenus = categoryMenuService.getListCategory();
        model.addAttribute("listCategoryMenu", categoryMenus);
        CategoryMenu categoryMenu = categoryMenuService.getCategoryMenuByCateID(cateId);
        List<Menu> listMenuDetailRes = menuService.getListMenusBySearch(categoryMenu, "", "", "", restaurant);
        model.addAttribute("listMenuDetailRes", listMenuDetailRes);
        Order order = new Order();
        model.addAttribute("order", order);
        return "customer/detailRestaurant";
    }
    @PostMapping("/customer/searchMenu")
    public String getListMenuBySearch(@RequestParam(name = "resID") int resID, @RequestParam(required = false, name = "priceFrom", defaultValue = "" ) String priceFrom,
                                      @RequestParam(required = false, name = "priceTo", defaultValue = "") String priceTo,
                                      @RequestParam(required = false, name = "foodName", defaultValue = "") String foodName,
                                      Model model) {


        Restaurant restaurant = restaurantService.getDetailRes(resID);
        model.addAttribute("detail", restaurant);
        List<CategoryMenu> categoryMenus = categoryMenuService.getListCategory();
        model.addAttribute("listCategoryMenu", categoryMenus);
        List<Menu> listMenuDetailRes = menuService.getListMenusBySearch(null, foodName, priceFrom, priceTo, restaurant);
        model.addAttribute("listMenuDetailRes", listMenuDetailRes);

        Order order = new Order();
        model.addAttribute("order", order);
        return "customer/detailRestaurant";
    }

    @PostMapping("/customer/order/{id}")
    public String infoOrder(@PathVariable int id, @ModelAttribute("order") Order order, HttpSession session){

        Customer customer = (Customer) session.getAttribute("customer");

        order.setCustomer(customer);
        order.setOrderStatus("wait pay");

        Restaurant restaurant = restaurantService.getDetailRes(id);
        order.setRestaurant(restaurant);
        orderService.save(order);

        return "redirect:/restaurant/customer/" + id;
    }

    @PostMapping("/customer/rateRes/{id}")
    public String RateRestaurant(@PathVariable int id, @RequestParam("star") int ratingPoint, @RequestParam("comment") String comment, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        Restaurant restaurant = restaurantService.getDetailRes(id);
        RateId rateId = new RateId(restaurant, customer);
        Rate rate = new Rate(rateId, comment, ratingPoint);
        rateService.saveRate(rate);
        return "redirect:/restaurant/customer/" + id;
    }
}
