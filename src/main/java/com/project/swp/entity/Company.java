package com.project.swp.entity;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompanyID")
    private int companyID;
    @Column(name = "CompanyName", length = Integer.MAX_VALUE)
    private String companyName;

    @Column(name = "Password", length = 20)
    private String password;

    @Column(name = "Hotline", length = 15)
    private String hotline;

    @Column(name = "Email", length = 30)
    private String email;

    @Column(name = "Status", length = 30)
    private String status;

    @Column(name = "BussinessLicense", length = Integer.MAX_VALUE)
    private String bussinessLicense;
}
