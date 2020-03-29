package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.ProductAlreadyExistsException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.ProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AdminServicesImp implements AdminServices {

    private ProductRepo productRepo;
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
            int qty = userServices.getProductQuantity(product.getProdId());
            productNode.put("qty", qty);

            productNodes.add(productNode);
        }

        node.put("products", productNodes);

        return node;
    }

    public boolean createProduct(Product product) {
        //Verify that product does not exist
        Optional<Product> findProduct = productRepo.findByName(product.getName());

        if(!findProduct.isPresent()) {
            productRepo.save(product);
        } else
            throw new ProductAlreadyExistsException();

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

    public boolean savePurchaseOrder(String body) {

        JSONObject obj = new JSONObject(body);
        String received = obj.getJSONObject(("po")).getString("recieved");

        JSONArray arr = obj.getJSONArray("lots");

        for (int i = 0; i < arr.length(); i++) {
            Lot lot = new Lot();
            int qty = arr.getJSONObject(i).getInt("qty");
            double cost = arr.getJSONObject(i).getDouble("cost");
            String bestBefore = arr.getJSONObject(i).getString("bestBefore");
            long prodId = arr.getJSONObject(i).getLong("prodId");
        }


        return true;
    }

    public void restoreBackup(Long id) {

    }

    public boolean saveProduct(Product product) {
        return false;
    }

    public boolean saveWarehouse(Warehouse warehouse) {
        return false;
    }

}
