package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Backup;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class AdminRestController {

    private final String URL = "/admin/";

    @GetMapping(URL + "inventory/all")
    public List<Product> getAllProducts() {
        return null;
    }

    @GetMapping(URL + "businessorder/pending")
    public List<Invoice> getPendingBusinessOrder() {
        return null;
    }

    @GetMapping(URL + "report/create")
    public void createReport(String type, Date start, Date end) {

    }

    @GetMapping(URL + "backup/export")
    public void exportData(Date start, Date end) {

    }

    @GetMapping(URL + "sales/all")
    public List<Invoice> getAllSales() {
        return null;
    }

    @GetMapping(URL + "discount/create")
    public boolean createDiscount(Discount discount) {
        return false;
    }

    @GetMapping(URL + "backup/get")
    public List<Backup> getBackupList() {
        return null;
    }

    @GetMapping(URL + "backup/restore")
    public void restoreBackup(Long id) {

    }

    @GetMapping(URL + "orders/get/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

}
