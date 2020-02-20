package ca.sait.vezorla.service;

import java.util.Date;
import java.util.List;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Backup;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.model.ProductQuantity;
import ca.sait.vezorla.model.PurchaseOrder;
import ca.sait.vezorla.model.Warehouse;

public interface AdminServices {

    public void acceptBusinessOrder(Invoice invoice);
    public boolean createAccount(Account account);
    public boolean createDiscount(Discount discount);
    public void createReport(String type, Date start, Date end);
    public void declineBusinessOrder(Long id);
    public void exportData(Date start, Date end);
    public void generatePDF(List<ProductQuantity> productQuantityList);
    public void get(Long id);
    public void getAllProducts();
    public List<Backup> getBackupList();
    public void getInvoice(Long id);
    public List<Invoice> getOrder(Long id);
    public List<Invoice> getPendingBusinessOrder();
    public Invoice getPendingBusinessOrderById(Long id);
    public boolean savePurchaseOrder(PurchaseOrder purchaseOrder);
    public void restoreBackup(Long id);
    public boolean saveProduct(Product product);
    public boolean saveWarehouse(Warehouse warehouse);
}
