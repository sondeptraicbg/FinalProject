package com.project.swp.controller;

import com.project.swp.entity.Company;
import com.project.swp.service.BossService;
import com.project.swp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;
import java.util.Random;

@Controller
@RequestMapping("/boss")

public class BossController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    JavaMailSender javaMailSender;

    @PostMapping("/approve")
    public String ApproveCompany(@RequestParam("companyId") int companyId,
                                 @RequestParam("status") String status) {
        Company company = companyService.findCompanyById(companyId);
        company.setStatus(status);
        String password = "";
        if(company.getPassword() ==null || company.getPassword().isEmpty()){
            Random r = new Random();
            String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for (int i = 0; i < 8; i++) {
                password += chars.charAt(r.nextInt(chars.length()));
            }
            company.setPassword(password);
        }
        String email = company.getEmail();
        ///////////////////////////////code////////////
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("New Password");
        message.setText("Your new password is: " + password);
        javaMailSender.send(message);
        companyService.SaveCompany(company);
        return "redirect:/home/boss";
    }

    @GetMapping("/company/{id}")
    public String DetailCompany(@PathVariable("id") int companyId, Model model) {
        Company company = companyService.findCompanyById(companyId);
        model.addAttribute("company", company);
        String url = company.getBussinessLicense();
        String[] parts = url.split("/");
        String fileId = parts[5];
        model.addAttribute("fileId", fileId);
        return "boss/detailCompany";
    }
}
