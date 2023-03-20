package com.project.swp.service;

import com.project.swp.entity.Invoice;
import com.project.swp.entity.InvoiceID;
import com.project.swp.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepo invoiceRepo;

    public void saveInvoice(Invoice invoice) {
        invoiceRepo.save(invoice);
    }

    public List<Invoice> getListInvoiceByOrderId(int orderId) {
        return invoiceRepo.findAllByInvoiceID_Order_OrderId(orderId);
    }


    public Invoice searchInvoice(InvoiceID invoiceID) {
        return  invoiceRepo.getInvoiceByInvoiceID(invoiceID).orElse(null);
    }
}
