package ca.sait.vezorla.service;

import java.util.Date;
import java.util.List;

import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;

public interface AdminServices {

    void acceptBusinessOrder(Invoice invoice);
    boolean createAccount(Account account);
    boolean createDiscount(Discount discount);
    void createReport(String type, Date start, Date end);
    void declineBusinessOrder(Long id);
    void exportData(Date start, Date end);
    void generatePDF(List<ProductQuantity> productQuantityList);
    void get(Long id);
    List<Backup> getBackupList();
    void getInvoice(Long id);
    List<Invoice> getOrder(Long id);
    List<Invoice> getPendingBusinessOrder();
    Invoice getPendingBusinessOrderById(Long id);
    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);
    void restoreBackup(Long id);
    boolean saveProduct(Product product);
    boolean saveWarehouse(Warehouse warehouse);
    ObjectNode getAllProducts(ObjectMapper mapper);
    boolean receivePurchaseOrder(String body);
    boolean saveLots(List<Lot> lots, PurchaseOrder po);
}
