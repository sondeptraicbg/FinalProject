package com.project.swp.controller;

import com.project.swp.entity.*;
import com.project.swp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/{id}")
    public String getOrderDetail(@PathVariable int id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "collection/detailOrder";
    }

    @GetMapping("/new/{id}")
    public String newOrder(@PathVariable("id") int id, Model model, HttpSession session) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        Restaurant restaurant = restaurantService.getDetailRes(id);
        model.addAttribute("detail", restaurant);
        return "manager/addNewOrder";
    }

    @PostMapping("/new/{id}")
    public String addNewOrder(@PathVariable("id") int id, @ModelAttribute("order") Order order, HttpSession session) {
        Restaurant restaurant = restaurantService.getDetailRes(id);
        order.setOrderStatus("Wait Pay");
        order.setRestaurant(restaurant);
        orderService.save(order);
        return "redirect:/home/manager";
    }

    @GetMapping("/update/{id}")
    public String updateOrder(@PathVariable("id") int id, Model model, HttpSession session) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        List<Tableq> tableqs = tableService.getTableByResId(restaurant.getResID());
        model.addAttribute("tableqs", tableqs);

        List<OrderDetail> listOrderDetailByOrderID = orderDetailService.getListOrderDetailByOrderID(id);
        model.addAttribute("listOrderDetail", listOrderDetailByOrderID);

        List<Menu> listMenu = menuService.getListMenuByResId(restaurant.getResID());
        model.addAttribute("listMenu", listMenu);

        return "manager/updateOrder";
    }

    @PostMapping("/update")
    public String updateMenu(@ModelAttribute("order") Order order, @RequestParam(value = "tb") List<String> tableIds, HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staff");
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");
        order.setRestaurant(restaurant);
        order.setStaff(staff);
        order.setStringTable(String.join(",", tableIds));
        order.setStaff(staff);
        orderService.save(order);
        return "redirect:/home/manager";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrderById(@PathVariable("id") int id, RedirectAttributes ra) {
        try {
            orderService.deleteById(id);
        } catch (Exception ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/home/manager";
    }


    //    ===   Add Menu Into Order =============================================
    @PostMapping("/addMenu/{orderId}")
    public String addMenuToOrder(@PathVariable("orderId") int orderId, @RequestParam("foodIds") List<Integer> foodIds, @RequestParam("quantity") List<Integer> quantity) throws Exception {
        Order order = orderService.getOrderById(orderId);

        List<OrderDetail> orderDetails = new ArrayList<>();

        List<Integer> validQuantities = new ArrayList<>();
        for (int item : quantity) {
            if (item > 0) {
                validQuantities.add(item);
            }
        }
        for(int i = 0; i < foodIds.size(); i++) {
            Menu menu = menuService.getById(foodIds.get(i));
            if (order.getTotal() == null)
                order.setTotal(0.0);
            order.setTotal(order.getTotal() + validQuantities.get(i) * menu.getPrice());
            orderService.save(order);
            OrderDetail orderDetail = new OrderDetail();
            OrderDetailID orderDetailID = new OrderDetailID(order, menu);
            orderDetail.setOrderDetailID(orderDetailID);
            orderDetail.setQuantity(validQuantities.get(i));
            orderDetailService.AddOrderDetail(orderDetail);
            orderDetails.add(orderDetail);
        }
        return "redirect:/order/update/" + orderId;
    }
}
