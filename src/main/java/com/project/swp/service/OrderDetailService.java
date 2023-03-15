package com.project.swp.service;

import com.project.swp.entity.OrderDetail;
import com.project.swp.entity.OrderDetailID;
import com.project.swp.repository.OrderDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepo orderDetailRepo;

    public void AddOrderDetail(OrderDetail orderDetail) {
        orderDetailRepo.save(orderDetail);
    }

    public List<OrderDetail> getListOrderDetailByOrderID(int orderID) {
        return orderDetailRepo.getAllByOrderDetailID_OrderId_OrderId(orderID);
    }

    public OrderDetail searchOrderDetail(OrderDetailID orderDetailID) {
        return  orderDetailRepo.getOrderDetailByOrderDetailID(orderDetailID).orElse(null);
    }
}
