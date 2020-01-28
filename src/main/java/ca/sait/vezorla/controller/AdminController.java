package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AdminController.URL)
public class AdminController {

    protected final static String URL = "/admin/";

    @GetMapping("inventory")
    public String getInventoryPage() {
        return null;
    }

    @GetMapping("inventory/create")
    public String getCreateProductPage() {
        return null;
    }

    @GetMapping("inventory/create/product")
    public void createProduct(Product product, Model model, BindingResult bindingResult) {

    }

    @GetMapping("inventory/update")
    public String getProductUpdatePage() {
        return null;
    }

    @GetMapping("inventory/update/product/{id}")
    public String updateProduct(@PathVariable Long id, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("warehouse/create")
    public String createWarehousePage() {
        return null;
    }

    @GetMapping("warehouse/create/warehouse")
    public String createWarehouse(Warehouse warehouse, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("report")
    public String getReportPage() {
        return null;
    }

    @GetMapping("purchases/create")
    public String getCreatePurchasePage() {
        return null;
    }

    @GetMapping("purchases/create/order")
    public String createPurchaseOrder(PurchaseOrder purchaseOrder, String to, String additionText, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("businessorder")
    public String getBusinessOrderPage(Long id, Model model) {
        return null;
    }

    @GetMapping("businessorder/decline")
    public String declineBusinessOrder(Long id, String additionText, Model model) {
        return null;
    }

    @GetMapping("businessorder/accept")
    public void acceptBusinessOrder(Invoice invoice) {

    }

    @GetMapping("account/create")
    public boolean createAccount(Account account) {
        return false;
    }

    @GetMapping("settings")
    public String getSettingsPage() {
        return null;
    }

    @GetMapping("account/update")
    public String updateAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("account/create/retail/confirm")
    public String getCreateRetailAccountConfirmPage(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("discounts/create")
    public String getCreateDiscountPage() {
        return null;
    }

    @GetMapping("account/create/get")
    public String getCreateAccount() {
        return null;
    }

    @GetMapping("backup/restore")
    public String getRestoreBackupPage() {
        return null;
    }

    @GetMapping("orders/history")
    public String getOrderHistoryPage() {
        return null;
    }

    @GetMapping("orders/invoice/{id}")
    public Invoice getInvoice(@PathVariable Long id) {
        return null;
    }

}
