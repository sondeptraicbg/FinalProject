package com.project.swp.repository;

import com.project.swp.entity.Order;
import com.project.swp.entity.OrderDetail;
import com.project.swp.entity.OrderDetailID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepo extends CrudRepository<OrderDetail, OrderDetailID> {
    List<OrderDetail> getAllByOrderDetailID_OrderId_OrderId(int id);

    Optional<OrderDetail> getOrderDetailByOrderDetailID(OrderDetailID orderDetailID);
}
