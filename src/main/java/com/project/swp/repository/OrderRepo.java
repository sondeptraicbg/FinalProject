package com.project.swp.repository;

import com.project.swp.DTO.RevenueDTO;
import com.project.swp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    // Manager //
    List<Order> findByRestaurant_ResID(int resID);

    Order findByOrderId(int id);

    Order deleteByOrderId(int orderId);

    List<Order> findByCustomer_CusIDOrderByOrderStatusDesc(int  cusID);

    @Query("select max (o.orderId) from Order o")
    int getOrderIdNLastest();

    List<Order> findAllByCustomer_CusID(int cusId);

//    @Modifying
//    @Query("Update Order o set o.table.tableId.restaurant.resID = :resID where o.orderId = :orderID")
//    void setResID(@Param("resID") int resID, @Param("orderID") int orderId);

    // Admin // ===============================================================================================

//    @Query(value = "SELECT YEAR(time_order) AS year, MONTH(time_order) AS month, SUM(total) AS revenue "
//            + "FROM orders "
//            + "WHERE resid = :restaurantId "
//            + "GROUP BY YEAR(time_order), MONTH(time_order)", nativeQuery = true)
//    List<RevenueDTO> getRevenueByMonthAndYearAndResId(@Param("restaurantId") Integer restaurantId);
//
//    @Query(value = "SELECT YEAR(time_order) AS year, MONTH(time_order) AS month, SUM(total) AS revenue "
//            + "FROM orders "
//            + "WHERE resid = :restaurantId "
//            + "GROUP BY YEAR(time_order), MONTH(time_order)", nativeQuery = true)
//    List<RevenueDTO> getRevenueByMonthAndYearAndComId(@Param("restaurantId") Integer restaurantId);

}
