package com.project.swp.service;

import com.project.swp.DTO.RevenueDTO;
import com.project.swp.entity.Restaurant;
import com.project.swp.entity.Revenue;
import com.project.swp.repository.OrderRepo;
import com.project.swp.repository.RestaurantRepo;
import com.project.swp.repository.RevenueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminHomeService {
    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RevenueRepo revenueRepo;

    public List<Restaurant> getAllRestaurantByCompany(int id){
        return restaurantRepo.findByCompany_CompanyID(id);
    }

    public List<Revenue> getRevenueByMonth(int redId){
        return revenueRepo.findByRestaurant_ResID(redId);
    }

    public List<Revenue> getRevenueByRestaurantID(int resID){
        return revenueRepo.findRevenuesByRestaurant_ResID(resID).orElse(null);
    }
}
