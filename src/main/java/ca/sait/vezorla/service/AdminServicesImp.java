package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.ProductAlreadyExistsException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smattme.MysqlImportService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Service
public class AdminServicesImp implements AdminServices {

    private ProductRepo productRepo;
    private PurchaseOrderRepo purchaseOrderRepo;
    private LotRepo lotRepo;
    private WarehouseRepo warehouseRepo;
    private InvoiceRepo invoiceRepo;
    private DiscountRepo discountRepo;
    private UserServices userServices;

    public void acceptBusinessOrder(Invoice invoice) {

    }

    public boolean createAccount(Account account) {
        return false;
    }

    /**
     * Create a new discount
     *
     * @param discount to add
     * @return <code>true</code> is successful, otherwise false
     * @author jjrr1717
     */
    public boolean createDiscount(@RequestBody Discount discount) {
        discountRepo.save(discount);
        return true;
    }

    public void createReport(String type, Date start, Date end) {

    }

    public void declineBusinessOrder(Long id) {

    }

    /**
     * Backup the Vezorla database
     *
     * @return <code>true</code> if successful
     * @author jjrr1717, matthewjflee
     */
    public boolean exportData() {
        boolean result = false;
        String dbName = "vezorla";
        String dbUser = "root";
        String dbPass = "pass";


        //C:/Program Files/MySQL/MySQL Server 8.0/bin/
        String executeCmd;
        executeCmd = "\"C:\\Program Files\\MySQL\\MySQL Workbench 8.0 CE\\mysqldump.exe\" -u" + dbUser + " -p" + dbPass + " " + dbName + " > C:\\Users\\783661\\Documents\\dumps\\backup3.sql";
        System.out.println(executeCmd);
        try {
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                result = true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void generatePDF(List<ProductQuantity> productQuantityList) {

    }

    public void get(Long id) {

    }

    /**
     * Method to get all the products for admin view
     *
     * @param mapper for custom json
     * @return ObjectNode of custom json
     * @author jjr1717
     */
    public ObjectNode getAllProducts(ObjectMapper mapper) {
        CustomerClientUtil ccu = new CustomerClientUtil();
        //obtain all the products
        List<Product> products = productRepo.findAll();

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode productNodes = node.arrayNode();

        //loop through products to get invoice details
        for (Product product : products) {
            ObjectNode productNode = productNodes.objectNode();
            productNode.put("prodId", product.getProdId());
            productNode.put("name", product.getName());
            productNode.put("imageMain", product.getImageMain());

            //Parse price
            long price = Long.parseLong(product.getPrice());
            productNode.put("price", ccu.formatAmount(price));

            //get total quantity
            Integer qty = userServices.getProductQuantity(product.getProdId());
            productNode.put("qty", qty);

            productNodes.add(productNode);
        }

        node.put("products", productNodes);

        return node;
    }

    /**
     * Create a new product in the Products table
     * Will check if the product exists in the database
     *
     * @param product product to create
     * @return <code>true</code> if the product exists
     * Will throw ProductAlreadyExistsException if the product already exists
     */
    public boolean createProduct(Product product) throws InvalidInputException {
        //Validate
        if (product.getName() == null || product.getPrice() == null)
            throw new InvalidInputException();

        //Verify that product does not exist
        Optional<Product> findProduct = productRepo.findByName(product.getName());

        if (!findProduct.isPresent()) {
            productRepo.save(product);
        } else
            throw new ProductAlreadyExistsException();

        return true;
    }

    public boolean saveProduct(Product product) {
        productRepo.save(product);
        return true;
    }


    public List<Backup> getBackupList() {
        return null;
    }

    public void getInvoice(Long id) {

    }

    public ObjectNode viewOrder(Long id, ObjectMapper mapper) {
        Invoice invoice = null;
        ObjectNode node = mapper.createObjectNode();
        Optional<Invoice> findInvoice = invoiceRepo.findById(id);
        if(findInvoice.isPresent())
            invoice = findInvoice.get();

        Account account = Objects.requireNonNull(invoice).getAccount();

        //Create JSON
        node.put("invoiceNum", invoice.getInvoiceNum());
        String date = invoice.getDate() + "";
        node.put("date", date);
        node.put("pickup", invoice.isPickup());
        node.put("shipped", invoice.isShipped());
        node.put("shippingCost", invoice.getShippingCost());
        node.put("subtotal", invoice.getSubtotal());
        node.put("discount", invoice.getDiscount());
        node.put("taxes", invoice.getTaxes());
        node.put("total", invoice.getTotal());

        node.put("email", account.getEmail());
        node.put("firstName", account.getFirstName());
        node.put("lastName", account.getLastName());
        node.put("phoneNum", account.getPhoneNum());
        node.put("address", account.getAddress());
        node.put("postalCode", account.getPostalCode());

        return node;
    }

    public List<Invoice> getPendingBusinessOrder() {
        return null;
    }

    public Invoice getPendingBusinessOrderById(Long id) {
        return null;
    }

    /**
     * Method to save purchase order to database
     *
     * @param purchaseOrder to save to database
     * @return purchase order saved to database
     * @author jjrr1717
     */
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepo.save(purchaseOrder);
    }

    /**
     * Method to save all the lots from
     * a purchase order to the database.
     *
     * @param lots to be saved
     * @return boolean true if all of them saved
     * successfully, otherwise false.
     * @author jjrr1717
     */
    public boolean saveLots(List<Lot> lots, PurchaseOrder po) {
        boolean result = false;
        int counter = 1;
        for (Lot lot : lots) {
            lot.setPurchaseOrder(po);
            lot.setLotNum(po.getPoNum() + "-" + counter);
            lotRepo.save(lot);
            counter++;
        }
        return true;
    }

    /**
     * Method to parse out the po from the json
     * file returned from front end.
     *
     * @param body the json
     * @return boolean true is everything processed successfully,
     * otherwise false;
     * @author jjrr1717
     */
    public boolean receivePurchaseOrder(String body) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        //parse json
        JSONObject obj = new JSONObject(body);
        String received = obj.getJSONObject(("po")).getString("received");
        JSONArray arr = obj.getJSONArray("lots");

        //get lot list from PO
        List<Lot> lots = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            Lot lot = new Lot();
            int qty = arr.getJSONObject(i).getInt("qty");
            double cost = arr.getJSONObject(i).getDouble("cost");
            String bestBefore = arr.getJSONObject(i).getString("bestBefore");
            long prodId = arr.getJSONObject(i).getLong("prodId");
            long warehouseNum = arr.getJSONObject(i).getLong("warehouseNum");

            //get product & warehouse by their id's
            Optional<Product> product = productRepo.findById(prodId);
            Optional<Warehouse> warehouse = warehouseRepo.findById(warehouseNum);

            //convert cost to cents
            long costLong = (long) (cost * 100);

            //convert bestBefore to Date
            Date date = null;
            try {
                date = format.parse(bestBefore);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            //set the values into a lot
            lot.setProduct(product.get());
            lot.setWarehouse(warehouse.get());
            lot.setQuantity(qty);
            lot.setCost(costLong);
            lot.setBestBefore(sqlDate);

            lots.add(lot);
        }
        //create po
        PurchaseOrder po = new PurchaseOrder();

        //convert date ordered
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
        po.setDateOrdered(sqlDate);

        //convert date received
        Date dateReceived = null;
        try {
            dateReceived = format.parse(received);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDateReceived;
        sqlDateReceived = new java.sql.Date(dateReceived.getTime());
        po.setDateReceived(sqlDateReceived);
        po.setLotList(lots);

        //save po to database
        PurchaseOrder savedPo = savePurchaseOrder(po);

        //save lots to database
        saveLots(lots, savedPo);

        return true;
    }

    /**
     * Restore the Vezorla database
     *
     * @param file file to restore
     * @return <code>true</code> if successful
     * @author jjrr1717, matthewjflee
     */
    public boolean restoreBackup(MultipartFile file) {
        boolean result = false;
        try {
            String sql = new String(file.getBytes());
            result = MysqlImportService.builder()
                    .setDatabase("vezorla")
                    .setSqlString(sql)
                    .setUsername("root")
                    .setPassword("pass")
                    .setDeleteExisting(true)
                    .setDropExisting(true)
                    .importDatabase();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            result = true;
        }

        return result;
    }

    public boolean saveWarehouse(Warehouse warehouse) {
        return false;
    }

    /**
     * Method to view the order history on a client's
     * account.
     *
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     * @author jjrr1717
     */
    public ObjectNode viewOrderHistoryAdmin(ObjectMapper mapper, HttpSession session) {
        CustomerClientUtil ccu = new CustomerClientUtil();

        //obtain all the invoices for account
        List<Invoice> invoices = invoiceRepo.findTop50ByOrderByInvoiceNumDesc();

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode invoiceNodes = node.arrayNode();

        //loop through invoices to get invoice details
        for (Invoice invoice : invoices) {
            ObjectNode invoiceNode = invoiceNodes.objectNode();
            invoiceNode.put("invoiceNum", invoice.getInvoiceNum());
            invoiceNode.put("total", ccu.formatAmount(invoice.getTotal()));
            String date = invoice.getDate() + "";
            invoiceNode.put("date", date);
            invoiceNodes.add(invoiceNode);
        }

        node.put("invoices", invoiceNodes);

        return node;
    }

    /**
     * Method to create and save warehouse to database
     * @param warehouse to save to database
     * @return <code>true</code> is successful, otherwise
     * false.
     * @author jjrr1717
     */
    public boolean createWarehouse(Warehouse warehouse) throws InvalidInputException {
        CustomerClientUtil ccu = new CustomerClientUtil();
        ccu.validatePhoneNumber(warehouse.getPhoneNumber());
        ccu.validatePostalCode(warehouse.getPostalCode());
        warehouseRepo.save(warehouse);
        return true;
    }
}
