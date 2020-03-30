package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Backup;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.AdminServices;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(AdminRestController.URL)
public class AdminRestController {

    protected final static String URL = "api/admin/";

    private AdminServices adminServices;
    private UserServices userServices;

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
     * View a single product
     * @param id ID of product
     * @return product info
     * @throws JsonProcessingException thrown if there is an error parsing JSON
     * @author matthewjflee
     */
    @GetMapping("inventory/product/{id}")
    public String getProduct(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getProduct(id, mapper));
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

    @PutMapping("inventory/update")
    public void updateProduct(@RequestBody Product sendProduct) {
        //Find product
        Product product = null;
        Optional<Product> findProduct = userServices.getProduct(sendProduct.getProdId());
        if(findProduct.isPresent())
            product = findProduct.get();

        Product updateProduct = adminServices.updateProduct(product, sendProduct);
//        adminServices.saveProduct(updateProduct);
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
