package com.project.swp.service;

import com.project.swp.entity.Rate;
import com.project.swp.repository.RateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService {
    @Autowired
    private RateRepo rateRepo;

    public List<Rate> getListRateByResId(int resId) {
        return rateRepo.findByRateId_Restaurant_ResID(resId);
    }

    public  void saveRate(Rate rate) {
        rateRepo.save(rate);
    }
}
