package com.project.swp.repository;

import com.project.swp.entity.Invoice;
import com.project.swp.entity.InvoiceID;
import com.project.swp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepo extends CrudRepository<Invoice, InvoiceID> {
    List<Invoice> findAll();

    List<Invoice> findAllByInvoiceID_Order_OrderId(int orderID);

    Optional<Invoice> getInvoiceByInvoiceID(InvoiceID invoiceID);
}
