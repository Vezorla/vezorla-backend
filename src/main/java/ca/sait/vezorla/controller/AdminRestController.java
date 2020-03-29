package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Backup;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.AdminServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(AdminRestController.URL)
public class AdminRestController {

    protected final static String URL = "api/admin/";

    private AdminServices adminServices;

    /**
     * Method to get all the products for admin view
     * @return String for the custom json
     * @throws JsonProcessingException
     */
    @GetMapping("inventory/all")
    public String getAllProducts() throws JsonProcessingException {
       ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.getAllProducts(mapper));

    }

    @PutMapping("receive_purchase_order")
    public boolean receivePurchaseOrder(@RequestBody String body){
        adminServices.receivePurchaseOrder(body);
        return true;
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
    public boolean restoreBackup(Long id) {
        return false;
    }

    @GetMapping("orders/get/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

}
