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

    @Autowired
    private InvoiceService invoiceService;

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


        List<Invoice> listInvoiceByOrderID = invoiceService.getListInvoiceByOrderId(id);
        model.addAttribute("listInvoice", listInvoiceByOrderID);

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
        return "redirect:/order/update/" + order.getOrderId();
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
    public String addMenuToOrder(@PathVariable("orderId") int orderId, @RequestParam("quantity") List<Integer> quantity, HttpSession httpSession) throws Exception {
        Order order = orderService.getOrderById(orderId);

        Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurant");
        List<Menu> listMenuByRestaurent = menuService.getListMenuByResId(restaurant.getResID());

        for(int i = 0; i < listMenuByRestaurent.size(); i++) {
            if(quantity.get(i) > 0) {
                Menu menu = listMenuByRestaurent.get(i);
                int quan = quantity.get(i);
                ///////////////////////      Oder Detail       /////////////////////////////
                OrderDetailID orderDetailID = new OrderDetailID(order, menu);
                OrderDetail orderDetail = orderDetailService.searchOrderDetail(orderDetailID);
                if(orderDetail == null) {
                    orderDetail = new OrderDetail(orderDetailID, quan);
                    orderDetailService.AddOrderDetail(orderDetail);
                }
                ///////////////////////      Invoice       /////////////////////////////
                InvoiceID invoiceID = new InvoiceID(order, menu);
                Invoice invoice = invoiceService.searchInvoice(invoiceID);
                if(invoice == null) {
                    invoice = new Invoice();
                    invoice.setInvoiceID(invoiceID);
                    invoice.setStaff((Staff) httpSession.getAttribute("staff"));
                    invoice.setDiscount(0.15F);
                    invoice.setActualQuantity(quan);
                    invoice.setActualTotal((float) (quan * (1.0 - invoice.getDiscount()) * menu.getPrice()));
                    invoiceService.saveInvoice(invoice);

                }else {
                    invoice.setActualQuantity(invoice.getActualQuantity() + quan);
                    invoice.setActualTotal((invoice.getActualTotal()) + (float) (quan * (1.0 - invoice.getDiscount()) * menu.getPrice()));
                }

                if (order.getTotal() == null)
                    order.setTotal(0.0);
                order.setTotal(order.getTotal() + invoice.getActualTotal());
                orderService.save(order);
            }
        }

        return "redirect:/order/update/" + orderId;
    }

    @GetMapping("/customer/view/{id}")
    public String CustomerViewOrder(@PathVariable("id") int id, Model model, HttpSession session) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "customer/detailOrder";
    }

    @PostMapping("/customer/edit")
    public String CustomerEditOrder(@ModelAttribute("order") Order order) {
        orderService.save(order);
        return "redirect:/order/customer/view/" + order.getOrderId();
    }

    @GetMapping("/customer/delete/{id}")
    public String CustomerDeleteOrder(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id);
        orderService.delteOrder(order);
        return "redirect:/customer/profile";
    }

    @GetMapping("/customer/pay/{id}")
    public String CustomerPay(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id);
        order.setOrderStatus("Done");
        orderService.save(order);
        return "redirect:/customer/profile";
    }
}
