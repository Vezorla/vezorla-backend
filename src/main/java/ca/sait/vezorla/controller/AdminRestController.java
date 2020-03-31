package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Backup;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.AdminServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
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
     * @throws JsonProcessingException thrown when there is an error parsing JSON
     * @author jjrr717
     */
    @GetMapping("inventory/all")
    public String getAllProducts() throws JsonProcessingException {
       ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.getAllProducts(mapper));
    }

    /**
     * Create a  new product in the database
     * @param product Product to create
     * @return <code>true</code> if saving is successful
     * Will throw a ProductAlreadyExistsException
     *
     * @author matthewjflee
     */
    @PostMapping("inventory/create")
    public boolean createProduct(@RequestBody Product product) throws InvalidInputException {
        return adminServices.createProduct(product);
    }

    @PostMapping("receive_purchase_order")
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
    public boolean exportData() {
        return adminServices.exportData();

    }

    @GetMapping("sales/all")
    public List<Invoice> getAllSales() {
        return null;
    }

    @PostMapping("discount/create")
    public boolean createDiscount(@RequestBody String body) {
        return false;
    }

    @GetMapping("backup/get")
    public List<Backup> getBackupList() {
        return null;
    }

    @GetMapping("backup/restore")
    public boolean restoreBackup(@RequestParam("file") MultipartFile body) {
        return adminServices.restoreBackup(body);
    }

    @GetMapping("orders/get/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

    @GetMapping("order_history")
    public String viewOrderHistoryAdmin(HttpServletRequest request) throws JsonProcessingException{
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewOrderHistoryAdmin(mapper, session));

    }

}
