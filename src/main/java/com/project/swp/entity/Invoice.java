package com.project.swp.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaRepositories
@EntityScan
@Data
@Table(name = "Invoice")
public class Invoice {
    @EmbeddedId
    private InvoiceID invoiceID;

    @Column(name = "ActualQuantity")
    private int actualQuantity;

    @Column(name = "Discount")
    private float discount;

    @Column(name = "ActualTotal")
    private float actualTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EmpID", referencedColumnName = "EmpID")
    private Staff staff;

    @Column(name = "TimePay")
    private String timePay;
}
