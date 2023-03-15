package com.project.swp.controller;

import com.project.swp.entity.Order;
import com.project.swp.entity.Restaurant;
import com.project.swp.entity.Staff;
import com.project.swp.entity.Tableq;
import com.project.swp.service.OrderService;
import com.project.swp.service.RestaurantService;
import com.project.swp.service.TableService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private TableService tableService;

    @GetMapping("/{id}")
    public String getOrderDetail(@PathVariable int id, Model model){
        Order order =  orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "collection/detailOrder";
    }

    @GetMapping("/new/{id}")
    public String newOrder(@PathVariable("id") int id, Model model, HttpSession session){
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        Restaurant restaurant = restaurantService.getDetailRes(id);
        model.addAttribute("detail", restaurant);
        return "manager/addNewOrder";
    }

    @PostMapping("/new/{id}")
    public String addNewOrder(@PathVariable("id") int id, @ModelAttribute("order") Order order, HttpSession session){
        Restaurant restaurant = restaurantService.getDetailRes(id);
        order.setOrderStatus("Wait pay");
        order.setRestaurant(restaurant);
        orderService.save(order);
        return "redirect:/home/manager";
    }

    @GetMapping("/update/{id}")
    public String updateOrder(@PathVariable("id") int id, Model model, HttpSession session){
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        List<Tableq> tableqs = tableService.getTableByResId(restaurant.getResID());
        model.addAttribute("tableqs", tableqs);
        return "manager/updateOrder";
    }

    @PostMapping("/update")
    public String updateMenu(@ModelAttribute("order") Order order,@RequestParam("tb") List<String> tableIds, HttpSession session){
        Staff staff = (Staff) session.getAttribute("staff");
        order.setStaff(staff);
        order.setStringTable(String.join(",", tableIds));
        if(order.getStaff() == null){
            order.setStaff(staff);
        }
        orderService.save(order);
        return "redirect:/home/manager";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrderById(@PathVariable("id") int id, RedirectAttributes ra){
        try{
            orderService.deleteById(id);
        }catch (Exception ex){
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/home/manager";
    }

}
