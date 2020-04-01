package ca.sait.vezorla.service;


import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface AdminServices {

    void acceptBusinessOrder(Invoice invoice);

    boolean createAccount(Account account);

    boolean createDiscount(Discount discount);

    void createReport(String type, Date start, Date end);

    void declineBusinessOrder(Long id);

    boolean exportData();

    void generatePDF(List<ProductQuantity> productQuantityList);

    ObjectNode getAllProducts(ObjectMapper mapper);

    ObjectNode getAllProductsForPO(ObjectMapper mapper);

    boolean createProduct(Product product) throws InvalidInputException;

    boolean saveProduct(Product product);

    List<Backup> getBackupList();

    void getInvoice(Long id);

    List<Invoice> getPendingBusinessOrder();

    Invoice getPendingBusinessOrderById(Long id);

    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);

    boolean saveWarehouse(Warehouse warehouse);

    boolean receivePurchaseOrder(String body);

    boolean saveLots(List<Lot> lots, PurchaseOrder po);

    ObjectNode viewOrder(Long id, ObjectMapper mapper);

    ObjectNode viewOrderHistoryAdmin(ObjectMapper mapper, HttpSession session);

    boolean restoreBackup(MultipartFile file);

    boolean createWarehouse(Warehouse warehouse) throws InvalidInputException;

    byte[] compressBytes(byte[] data);

    byte[] decompressBytes(byte[] data);

    void saveImage(Image image);

    Optional<Image> getImage(Long id);
  
    ObjectNode getAllWarehousesForPO(ObjectMapper mapper);

    ObjectNode getNextPONum(ObjectMapper mapper);
}
