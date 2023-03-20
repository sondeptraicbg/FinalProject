package com.project.swp.repository;

import com.project.swp.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RevenueRepo extends JpaRepository<Revenue, Integer> {
    List<Revenue> findByRestaurant_ResID(int redId);

    Optional<List<Revenue>> findRevenuesByRestaurant_ResID(int resId);
}
