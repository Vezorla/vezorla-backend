package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.ProductAlreadyExistsException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.LotRepo;
import ca.sait.vezorla.repository.ProductRepo;
import ca.sait.vezorla.repository.PurchaseOrderRepo;
import ca.sait.vezorla.repository.WarehouseRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
    private UserServices userServices;

    public void acceptBusinessOrder(Invoice invoice) {

    }

    public boolean createAccount(Account account) {
        return false;
    }

    public boolean createDiscount(Discount discount) {
        return false;
    }

    public void createReport(String type, Date start, Date end) {

    }

    public void declineBusinessOrder(Long id) {

    }

    public void exportData(Date start, Date end) {

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
            productNode.put("name", product.getName());
            productNode.put("imageMain", product.getImageMain());
            productNode.put("price", ccu.formatAmount(product.getPrice()));

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
        if(product.getName() == null || product.getPrice() == null)
            throw new InvalidInputException();

        //Verify that product does not exist
        Optional<Product> findProduct = productRepo.findByName(product.getName());

        if (!findProduct.isPresent()) {
            productRepo.save(product);
        } else
            throw new ProductAlreadyExistsException();

        return true;
    }

    /**
     * Update a product
     * Do not update a field if it is not present
     * @param product product to update
     * @param changed user sent product
     * @return product
     * @author matthewjflee
     */
    public Product updateProduct(Product product, Product changed) {
//        Optional<Product> checkNameProduct = productRepo.findByName(changed.getName());
//        if(checkNameProduct.isPresent())
//            throw new ProductAlreadyExistsException();
//         if (changed.getName() != null)
//            product.setName(changed.getName());

//        if(changed.getDescription() != null)
//            product.setDescription(changed.getDescription());
//
//        if(changed.getSubdescription() != null)
//            product.setSubdescription(changed.getSubdescription());
//
//        if(changed.getHarvestTime() != null)
//            product.setHarvestTime(changed.getHarvestTime());
//
//        if(changed.getImageMain() != null)
//            product.setImageMain(changed.getImageMain());
//
//        if(changed.getImageOne() != null)
//            product.setImageOne(changed.getImageOne());
//
//        if(changed.getImageTwo() != null)
//            product.setImageTwo(changed.getImageTwo());
//
//        if(changed.getImageThree() != null)
//            product.setImageThree(changed.getImageThree());
//
//        if(changed.getActive() != null)
//            product.setActive(changed.getActive());
//
//        if(changed.getThreshhold() != null)
//            product.setThreshhold(changed.getThreshhold());
//
//        if(changed.getPrice() != null)
//            product.setPrice(changed.getPrice());
//
//        if(changed.getOldPrice() != null)
//            product.setOldPrice(changed.getOldPrice());

        saveProduct(changed);

        return product;
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

    public List<Invoice> getOrder(Long id) {
        return null;
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


    public void restoreBackup(Long id) {

    }

    public boolean saveWarehouse(Warehouse warehouse) {
        return false;
    }

}
