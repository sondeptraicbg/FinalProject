package com.project.swp.service;

import com.project.swp.entity.Company;
import com.project.swp.entity.Restaurant;
import com.project.swp.entity.Staff;
import com.project.swp.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepo companyRepo;
    public List<Company> getListCompany() {
        return companyRepo.findAll();
    }
    public Company findCompanyById(int companyId) {
        return companyRepo.findCompanyByCompanyID(companyId).orElse(null);
    }

    // Authenticate admin // ======

    public Company authenticate(String email, String password){
        Company company = companyRepo.findFirstByEmailAndPassword(email, password);
        return company != null  ? company : null;
    }

    public void SaveCompany(Company company) {
        companyRepo.save(company);
    }

}
