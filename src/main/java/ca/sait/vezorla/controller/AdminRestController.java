package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Backup;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(AdminRestController.URL)
public class AdminRestController {

    protected final static String URL = "/admin/";

    @GetMapping("inventory/all")
    public List<Product> getAllProducts() {
        return null;
    }

    @GetMapping("businessorder/pending")
    public List<Invoice> getPendingBusinessOrder() {
        return null;
    }

    @GetMapping("report/create")
    public void createReport(String type, Date start, Date end) {

    }

    @GetMapping("backup/export")
    public void exportData(Date start, Date end) {

    }

    @GetMapping("sales/all")
    public List<Invoice> getAllSales() {
        return null;
    }

    @GetMapping("discount/create")
    public boolean createDiscount(Discount discount) {
        return false;
    }

    @GetMapping("backup/get")
    public List<Backup> getBackupList() {
        return null;
    }

    @GetMapping("backup/restore")
    public void restoreBackup(Long id) {

    }

    @GetMapping("orders/get/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

}
