package com.project.swp.repository;

import com.project.swp.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueRepo extends JpaRepository<Revenue, Integer> {
    List<Revenue> findByRestaurant_ResID(int redId);

    List<Revenue> findRevenuesByRestaurant_ResID(int resId);
}
