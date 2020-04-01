package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.service.AdminServices;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(AdminRestController.URL)
public class AdminRestController {

    protected final static String URL = "api/admin/";

    private AdminServices adminServices;
    private UserServices userServices;

    /**
     * Method to get all the products for admin view
     *
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
     *
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
     *
     * @param product Product to create
     * @return <code>true</code> if saving is successful
     * Will throw a ProductAlreadyExistsException
     * @author matthewjflee
     */
    @PostMapping("inventory/create")
    public boolean createProduct(@RequestBody Product product) throws InvalidInputException {
        return adminServices.createProduct(product);
    }

    /**
     * Update a product
     *
     * @param product product to update
     * @return <code>true</code> if successful, <code>false</code> if not
     * @author matthewjflee
     */
    @PutMapping("inventory/update")
    public boolean updateProduct(@RequestBody Product product) {
        //Fix date. Date comes in one day less so add one more day
        Calendar cal = Calendar.getInstance();
        cal.setTime(product.getHarvestTime());
        cal.add(Calendar.DATE, 1);
        java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
        product.setHarvestTime(date);

        //Parse the price
        float floPrice = Float.parseFloat(product.getPrice()) * 100;
        long price = (long) floPrice;


        String stringPrice = Objects.toString(price);
        product.setPrice(stringPrice);

        return adminServices.saveProduct(product);
    }

    @PostMapping("img/upload")
    public boolean uploadImage(@RequestParam("imgFile") MultipartFile file) throws IOException {
        byte[] imgCompressed = adminServices.compressBytes(file.getBytes());

        Image image = new Image(file.getOriginalFilename(), file.getContentType(), imgCompressed);

        adminServices.saveImage(image);

        return true;
    }

    @GetMapping("img/get/{id}")
    public Image getImage(@PathVariable("id") Long id) {
        Image rawImage = null;
        Optional<Image> findImage = adminServices.getImage(id);

        if(findImage.isPresent())
            rawImage = findImage.get();

        byte[] imgDecompressed = adminServices.decompressBytes(rawImage.getPicByte());

        Image image = new Image(rawImage.getId(), rawImage.getName(), rawImage.getType(), imgDecompressed);

        return image;
    }

    /**
     * Create a new purchase order
     * This will create lots
     *
     * @param body purchase order
     * @return <code>true</code> if it was created successfully
     * @author jjrr1717
     */
    @PostMapping("receive_purchase_order")
    public boolean receivePurchaseOrder(@RequestBody String body) {
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

    /**
     * Export the data in the Vezorla database
     *
     * @return sql dump of data
     * @author jjrr1717
     */
    @GetMapping("backup/export")
    public boolean exportData() {
        return adminServices.exportData();
    }

    /**
     * Method to create and save discount to database
     * @param discount to save
     * @return <code>true</code> if successful, otherwise
     * false.
     */
    @PostMapping("discount/create")
    public boolean createDiscount(@RequestBody Discount discount) {
        return adminServices.createDiscount(discount);
    }

    /**
     * Method to create and save warehouse to database
     * @param warehouse to create and save
     * @return <code>true</code> if successful, otherwise
     * false.
     */
    @PostMapping("warehouse/create")
    public boolean createWarehouse(@RequestBody Warehouse warehouse) throws InvalidInputException {
        return adminServices.createWarehouse(warehouse);
    }

    @GetMapping("backup/get")
    public List<Backup> getBackupList() {
        return null;
    }

    /**
     * Restore a previously taken backup to the Vezorla database
     *
     * @param file file to restore the database with
     * @return <code>true</code> if it was successful
     * @author jjrr1717
     */
    @GetMapping("backup/restore")
    public boolean restoreBackup(@RequestParam("file") MultipartFile file) {
        return adminServices.restoreBackup(file);
    }

    @GetMapping("orders/{id}")
    public String viewOrder(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewOrder(id, mapper));
    }

    @GetMapping("order_history")
    public String viewOrderHistoryAdmin(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewOrderHistoryAdmin(mapper, session));
    }
}
