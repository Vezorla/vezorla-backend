package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminServicesImp implements AdminServices {

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

    public void getAllProducts() {

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

    public boolean savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return false;
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
