package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.*;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.*;
import ca.sait.vezorla.service.AdminServices;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * AdminServicesImp class.
 *
 * This class implements the AdminServices interface.
 *
 * This class acts as the intermediary between the controllers
 * and the repositories.
 */
@AllArgsConstructor
@Service
public class AdminServicesImp implements AdminServices {

    private ProductRepo productRepo;
    private PurchaseOrderRepo purchaseOrderRepo;
    private LotRepo lotRepo;
    private WarehouseRepo warehouseRepo;
    private InvoiceRepo invoiceRepo;
    private DiscountRepo discountRepo;
    private AccountRepo accountRepo;
    private ImageRepo imgRepo;
    private UserServices userServices;

    /**
     * Constructor that uses an Invoice.
     *
     * @param invoice Invoice to be accepted.
     */
    public void acceptBusinessOrder(Invoice invoice) {

    }

    /**
     * Creates an Account in the database.
     *
     * @param account Account to be created.
     * @return Boolean true if created, false otherwise.
     */
    public boolean createAccount(Account account) {
        return false;
    }

    /**
     * Create a new discount.
     *
     * @param discount to add
     * @return <code>true</code> is successful, otherwise false
     * @author jjrr1717
     */
    public boolean createDiscount(@RequestBody Discount discount) {
        discountRepo.save(discount);
        return true;
    }

    /**
     * Creates a Report in the database.
     *
     * @param type Report type.
     * @param start Report start date.
     * @param end Report end date.
     */
    public void createReport(String type, Date start, Date end) {

    }

    /**
     * Declines a pending business order.
     *
     * @param id Invoice ID to be declined.
     */
    public void declineBusinessOrder(Long id) {

    }

    /**
     * Backup the Vezorla database.
     *
     * Source: https://dzone.com/articles/how-to-backup-mysql-database-programmatically-usin
     *
     * @return <code>true</code> if successful
     * @author jjrr1717, matthewjflee
     */
    public boolean exportData() {
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, "vezorla");
        properties.setProperty(MysqlExportService.DB_USERNAME, "root");
        properties.setProperty(MysqlExportService.DB_PASSWORD, "pass");


        properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.gmail.com");
        properties.setProperty(MysqlExportService.EMAIL_PORT, "587");
        properties.setProperty(MysqlExportService.EMAIL_USERNAME, "vezorla.test@gmail.com");
        properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "NGB6DGZ98oXt6XmwxD7Q45povRzXHqGX8");
        properties.setProperty(MysqlExportService.EMAIL_FROM, "vezorla.test@gmail.com");
        properties.setProperty(MysqlExportService.EMAIL_TO, "vezorla.test@gmail.com");

        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");

        //set the outputs temp dir
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());
        MysqlExportService mysqlExportService = new MysqlExportService(properties);
        mysqlExportService.getGeneratedZipFile();

        try {
            mysqlExportService.export();
        } catch (IOException | ClassNotFoundException | SQLException ignore) {
        }

        mysqlExportService.clearTempFiles(false);

        return true;
    }

    /**
     * Method to get all the products for admin view.
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
     * Method to get all the products for PO combo box.
     *
     * @param mapper for custom json
     * @return ObjectNode of custom json
     * @author jjr1717
     */
    public ObjectNode getAllProductsForPO(ObjectMapper mapper) {
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
            productNodes.add(productNode);
        }

        node.put("products", productNodes);

        return node;
    }

    /**
     * Method to get all the warehouses for PO combo box.
     *
     * @param mapper for custom json
     * @return ObjectNode of custom json
     * @author jjr1717
     */
    public ObjectNode getAllWarehousesForPO(ObjectMapper mapper) {
        //obtain all the products
        List<Warehouse> warehouses = warehouseRepo.findAll();

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode productNodes = node.arrayNode();

        //loop through products to get invoice details
        for (Warehouse warehouse : warehouses) {
            ObjectNode productNode = productNodes.objectNode();
            productNode.put("warehouseNum", warehouse.getWarehouseNum());
            productNode.put("address", warehouse.getAddress());
            productNode.put("city", warehouse.getCity());
            productNodes.add(productNode);
        }

        node.put("warehouses", productNodes);

        return node;
    }

    /**
     * Create a new product in the Products table.
     *
     * Will check if the product exists in the database.
     *
     * @param product product to create
     * @param session user session to grab the image
     * @return <code>true</code> if the product exists
     * Will throw ProductAlreadyExistsException if the product already exists
     * @author matthewjflee
     */
    public boolean createProduct(Product product, HttpSession session) throws InvalidInputException {
        //Validate
        if (product.getName() == null || product.getPrice() == null)
            throw new InvalidInputException();

        //Verify that product does not exist
        Optional<Product> findProduct = productRepo.findByName(product.getName());

        if (!findProduct.isPresent()) {
            //Grab the image
            Long imageID = (Long) session.getAttribute("IMAGE_ID");
            product.setImageMain(imageID);
            session.removeAttribute("IMAGE_ID");

            //Fix date
            java.sql.Date date = fixDate(product.getHarvestTime());
            product.setHarvestTime(date);

            //Fix price
            String price = parsePrice(product.getPrice());
            product.setPrice(price);

            productRepo.save(product);
        } else
            throw new ProductAlreadyExistsException();

        return true;
    }

    /**
     * Update a product.
     *
     * Do not update a field if it is not present.
     *
     * @param product product to update
     * @param changed user sent product
     * @return <code>true</code>> if successful
     * <code>false</code> if unsuccessful
     * @author matthewjflee
     */
    public boolean updateProduct(Product product, Product changed) {
        //Fix date and price
        java.sql.Date date = fixDate(changed.getHarvestTime());
        changed.setHarvestTime(date);

        String price = parsePrice(changed.getPrice());
        changed.setPrice(price);


        if (changed.getName() != null)
            product.setName(changed.getName());

        if (changed.getDescription() != null)
            product.setDescription(changed.getDescription());

        if (changed.getHarvestTime() != null)
            product.setHarvestTime(changed.getHarvestTime());

        if (changed.getActive() != null)
            product.setActive(changed.getActive());

        product.setThreshhold(changed.getThreshhold());

        if (changed.getPrice() != null)
            product.setPrice(changed.getPrice());

        Product saved = saveProduct(product);
        if (saved == null)
            return false;

        return true;
    }

    /**
     * Fix date.
     *
     * Date comes in one day less so add one more day.
     *
     * @param date input
     * @return fixed date
     * @author matthewjflee
     */
    private java.sql.Date fixDate(java.sql.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * Parse price to remove any decimals and multiply it by 100 in the database.
     *
     * @param inputPrice changed price
     * @return parsed price
     * @author matthewjflee
     */
    private String parsePrice(String inputPrice) {
        float floPrice = Float.parseFloat(inputPrice) * 100;
        long price = (long) floPrice;

        return Objects.toString(price);
    }

    /**
     * Save a product in the Products table.
     *
     * @param product product to save
     * @return if it has been saved
     * @author matthewjflee
     */
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    /**
     * View a single order.
     *
     * @param id invoice ID
     * @param mapper custom JSON
     * @return invoice in JSON format
     * @author jjrr1717
     */
    public ObjectNode viewOrder(Long id, ObjectMapper mapper) {
        Invoice invoice = null;
        ObjectNode node = mapper.createObjectNode();
        Optional<Invoice> findInvoice = invoiceRepo.findById(id);
        if (findInvoice.isPresent())
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

    /**
     * Gets all pending business orders from the database.
     *
     * @return List containing all pending Invoices.
     */
    public List<Invoice> getPendingBusinessOrder() {
        return null;
    }

    /**
     * Gets a pending business orders from the database
     * via id.
     *
     * @return Pending business order by id.
     */
    public Invoice getPendingBusinessOrderById(Long id) {
        return null;
    }

    /**
     * Method to save purchase order to database.
     *
     * @param purchaseOrder to save to database
     * @return purchase order saved to database
     * @author jjrr1717
     */
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepo.save(purchaseOrder);
    }

    /**
     * Method to get the next PO num.
     *
     * @param mapper for json
     * @return ObjectNode for json
     * @author jjrr1717
     */
    public ObjectNode getNextPONum(ObjectMapper mapper) {
        int nextPO = purchaseOrderRepo.findLastPO() + 1;
        ObjectNode node = mapper.createObjectNode();
        node.put("nextPO", nextPO);
        return node;
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
        int counter = 1;
        for (Lot lot : lots) {
            lot.setPurchaseOrder(po);
            lot.setLotNum(po.getPoNum() + "-" + counter);
            System.out.println(lot.getLotNum());
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

            assert date != null;
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
        assert dateReceived != null;
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
     * Restore the Vezorla database.
     *
     * @param file file to restore
     * @return <code>true</code> if successful
     * @author jjrr1717, matthewjflee
     */
    public boolean restoreBackup(MultipartFile file) throws OutOfStockException {
        boolean result;
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
        } catch (SQLException | ClassNotFoundException | IOException | StringIndexOutOfBoundsException e) {
            result = true;
        }

        return result;
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
			invoiceNode.put("email", invoice.getAccount().getEmail());
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
     * Method to create and save warehouse to database.
     *
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

    /**
     * Compress the bytes of an image to persist into the database.
     *
     * @param data image data in bytes
     * @return the compressed bytes of the image
     * @author matthewjflee
     */
    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outStream.write(buffer, 0, count);
        }

        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outStream.toByteArray();
    }

    /**
     * Decompress the bytes of an image to display.
     *
     * @param data image in bytes
     * @return bytes of the image decompressed
     * @author matthewjflee
     */
    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outStream.write(buffer, 0, count);
            }

            outStream.close();
        } catch (DataFormatException | IOException e) {
            e.printStackTrace();
        }

        return outStream.toByteArray();
    }

    /**
     * Persist an image in the database.
     *
     * @param image  image to persist
     * @param prodId product image to update
     * @author matthewjflee
     */
    public void saveImage(Image image, Optional<Long> prodId, HttpSession session) {
        Image saved = imgRepo.save(image);

        if (prodId.isPresent())
            updateProductImage(saved, prodId.get());
        else
            session.setAttribute("IMAGE_ID", saved.getId());
    }

    /**
     * Update an image.
     *
     * Go through the list of images and replace the oldest image
     * Sorry we are out of time I know this is ugly.
     *
     * @param image  image to save
     * @param prodId product
     * @author matthewjflee
     */
    private void updateProductImage(Image image, Long prodId) {
        Product product = null;
        Optional<Product> findProduct = userServices.getProduct(prodId);
        if (findProduct.isPresent())
            product = findProduct.get();

        //Update the product image
        List<Long> imageIdList = new ArrayList<>();

        assert product != null;
        if (product.getImageMain() != null)
            imageIdList.add(product.getImageMain());

        if (product.getImageOne() != null)
            imageIdList.add(product.getImageOne());

        if (product.getImageTwo() != null)
            imageIdList.add(product.getImageTwo());

        if (product.getImageThree() != null)
            imageIdList.add(product.getImageThree());

        //Find the image with the lowest ID
        int index = 0;
        int listSize = imageIdList.size();

        switch (listSize) {
            case 0:
                index = 0;
                break;

            case 1:
                index = 1;
                break;

            case 2:
                index = 2;
                break;

            case 3:
                index = 3;
                break;

            case 4:
                long lowest = imageIdList.get(0);
                for (int i = 1; i < imageIdList.size(); i++) {
                    if (lowest > imageIdList.get(i)) {
                        lowest = imageIdList.get(i);
                        index = i;
                    }
                }
                break;
        }

        switch (index) {
            case 0:
                product.setImageMain(image.getId());
                break;

            case 1:
                product.setImageOne(image.getId());
                break;

            case 2:
                product.setImageTwo(image.getId());
                break;

            case 3:
                product.setImageThree(image.getId());
                break;
        }

        //Update product
        saveProduct(product);
    }

    /**
     * Get an image in the database.
     *
     * @param id ID of the image
     * @return image
     * @author matthewjflee
     */
    public Optional<Image> getImage(Long id) {
        return imgRepo.findById(id);
    }

    /**
     * Method to return admin email to front end
     * for update admin account.
     *
     * @param session to get Account
     * @param mapper  for json
     * @return Object node for json
     * @throws UnauthorizedException email is invalid
     * @author jjrr1717
     */
    public ObjectNode getAdminEmail(HttpSession session, ObjectMapper mapper) throws UnauthorizedException {
        Account account = (Account) session.getAttribute("ACCOUNT");
        String adminEmail = account.getEmail();
        ObjectNode node;
        if (account.getAccountType() == 'A') {
            node = mapper.createObjectNode();
            node.put("email", adminEmail);
        } else {
            throw new UnauthorizedException();
        }
        return node;
    }

    /**
     * Method to view all the clients.
     *
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     * @author jjrr1717
     */
    public ObjectNode viewAllClients(ObjectMapper mapper) {

        //obtain all the client accounts
        List<Account> accounts = accountRepo.findAllUserCreatedAccounts();

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode clientNodes = node.arrayNode();

        //loop through accounts to get account details
        for (Account account : accounts) {
            ObjectNode accountNode = clientNodes.objectNode();
            accountNode.put("email", account.getEmail());
            accountNode.put("firstName", account.getFirstName());
            accountNode.put("lastName", account.getLastName());
            accountNode.put("phoneNum", account.getPhoneNum());

            clientNodes.add(accountNode);

        }

        node.put("clients", clientNodes);

        return node;
    }

    /**
     * Method to view a client.
     *
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     * @author jjrr1717
     */
    public ObjectNode viewClient(String email, ObjectMapper mapper) {

        //obtain all the client accounts
        Optional<Account> account = accountRepo.findById(email);
        ObjectNode accountNode;
        if (account.isPresent()) {
            //create custom json
            accountNode = mapper.createObjectNode();
            accountNode.put("email", account.get().getEmail());
            accountNode.put("accountType", Character.toString(account.get().getAccountType()));
            accountNode.put("firstName", account.get().getFirstName());
            accountNode.put("lastName", account.get().getLastName());
            accountNode.put("phoneNum", account.get().getPhoneNum());
            accountNode.put("address", account.get().getAddress());
            accountNode.put("city", account.get().getCity());
            accountNode.put("province", account.get().getProvince());
            accountNode.put("country", account.get().getCountry());
            accountNode.put("postalCode", account.get().getPostalCode());
            accountNode.put("isSubscript", account.get().getIsSubscript());
        } else {
            throw new AccountNotFoundException();
        }

        return accountNode;
    }
}
