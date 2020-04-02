package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.OutOfStockException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.service.AdminServices;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
     * Method to get all the products for admin view
     *
     * @return String for the custom json
     * @throws JsonProcessingException thrown when there is an error parsing JSON
     * @author jjrr717
     */
    @GetMapping("inventory/all/po")
    public String getAllProductsForPO() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.getAllProductsForPO(mapper));
    }

    /**
     * Method to get all the products for admin view
     *
     * @return String for the custom json
     * @throws JsonProcessingException thrown when there is an error parsing JSON
     * @author jjrr717
     */
    @GetMapping("warehouse/all/po")
    public String getAllWarehousesForPO() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.getAllWarehousesForPO(mapper));
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
     * Method to get the next PO num
     *
     * @return the json
     * @throws JsonProcessingException error when parsing JSON
     * @author jjrr1717
     */
    @GetMapping("purchase_order/next")
    public String getNextPONum() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.getNextPONum(mapper));
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
    public boolean createProduct(@RequestBody Product product, HttpServletRequest request) throws InvalidInputException {
        HttpSession session = request.getSession();

        return adminServices.createProduct(product, session);
    }

    /**
     * Update a product
     *
     * @param changed product to update
     * @return <code>true</code> if successful, <code>false</code> if not
     * @author matthewjflee
     */
    @PutMapping("inventory/update")
    public boolean updateProduct(@RequestBody Product changed) {
        Product product = null;
        Optional<Product> findProduct = userServices.getProduct(changed.getProdId());
        if (findProduct.isPresent())
            product = findProduct.get();

        return adminServices.updateProduct(product, changed);
    }

    /**
     * Upload an image to the database
     * Source: https://dzone.com/articles/upload-and-retrieve-filesimages-using-spring-boot
     *
     * @param file image
     * @return if the image was persisted
     * @throws IOException thrown when compressing the bytes
     * @author matthewjflee
     */
    @PostMapping("img/upload")
    public boolean uploadImage(@RequestParam("imgFile") MultipartFile file, @RequestParam("prodId") Optional<Long> prodId, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();

        byte[] imgCompressed = adminServices.compressBytes(file.getBytes());
        Image image = new Image(file.getOriginalFilename(), file.getContentType(), imgCompressed);

        adminServices.saveImage(image, prodId, session);

        return true;
    }

    /**
     * Get an image
     * Source: https://dzone.com/articles/upload-and-retrieve-filesimages-using-spring-boot
     *
     * @param id: ID of image
     * @return image
     * @author matthewjflee
     */
    @GetMapping("img/get/{id}")
    public Image getImage(@PathVariable("id") Long id) {
        Image rawImage = null;
        Optional<Image> findImage = adminServices.getImage(id);

        if (findImage.isPresent())
            rawImage = findImage.get();

        assert rawImage != null;
        byte[] imgDecompressed = adminServices.decompressBytes(rawImage.getPicByte());

        return new Image(rawImage.getId(), rawImage.getName(), rawImage.getType(), imgDecompressed);
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

    @GetMapping("business-order/pending")
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
     *
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
     *
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
    @PostMapping("backup/restore")
    public boolean restoreBackup(@RequestParam("file") MultipartFile file) throws OutOfStockException {
        return adminServices.restoreBackup(file);
    }

    @GetMapping("orders/{id}")
    public String viewOrder(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewOrder(id, mapper));
    }

    /**
     * Method to view all the orders from the
     * admin perspective
     *
     * @param request for the session
     * @return the json
     * @throws JsonProcessingException error when parsing JSON
     * @author jjrr1717
     */
    @GetMapping("order_history")
    public String viewOrderHistoryAdmin(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewOrderHistoryAdmin(mapper, session));
    }

    /**
     * Method to get admin email for the update admin account page
     *
     * @param request for the session
     * @param mapper  for the json
     * @return the json
     * @throws JsonProcessingException error when parsing JSON
     * @throws UnauthorizedException   if the email is invalid
     * @author jjrr1717
     */
    @GetMapping("email")
    public String getAdminEmail(HttpServletRequest request, ObjectMapper mapper) throws JsonProcessingException, UnauthorizedException {
        HttpSession session = request.getSession();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.getAdminEmail(session, mapper));
    }

    /**
     * Method to view all of the clients
     *
     * @return the clients to front-end
     * @author jjrr1717
     */
    @GetMapping("clients")
    public String viewAllClients() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewAllClients(mapper));
    }

    /**
     * Method to view a client
     *
     * @return the json of client to front end
     * @author jjrr1717
     */
    @GetMapping("client/{email}")
    public String viewClient(@PathVariable String email) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adminServices.viewClient(email, mapper));
    }
}
