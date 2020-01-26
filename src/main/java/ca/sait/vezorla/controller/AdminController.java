package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    private final String URL = "/admin/";

    @GetMapping(URL + "inventory")
    public String getInventoryPage() {
        return null;
    }

    @GetMapping(URL + "inventory/create")
    public String getCreateProductPage() {
        return null;
    }

    @GetMapping(URL + "inventory/create/product")
    public void createProduct(Product product, Model model, BindingResult bindingResult) {

    }

    @GetMapping(URL + "inventory/update")
    public String getProductUpdatePage() {
        return null;
    }

    @GetMapping(URL + "inventory/update/product/{id}")
    public String updateProduct(@PathVariable Long id, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "warehouse/create")
    public String createWarehousePage() {
        return null;
    }

    @GetMapping(URL + "warehouse/create/warehouse")
    public String createWarehouse(Warehouse warehouse, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "report")
    public String getReportPage() {
        return null;
    }

    @GetMapping(URL + "purchases/create")
    public String getCreatePurchasePage() {
        return null;
    }

    @GetMapping(URL + "purchases/create/order")
    public String createPurchaseOrder(PurchaseOrder purchaseOrder, String to, String additionText, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "businessorder")
    public String getBusinessOrderPage(Long id, Model model) {
        return null;
    }

    @GetMapping(URL + "businessorder/decline")
    public String declineBusinessOrder(Long id, String additionText, Model model) {
        return null;
    }

    @GetMapping(URL + "businessorder/accept")
    public void acceptBusinessOrder(Invoice invoice) {

    }

    @GetMapping(URL + "account/create")
    public boolean createAccount(Account account) {
        return false;
    }

    @GetMapping(URL + "settings")
    public String getSettingsPage() {
        return null;
    }

    @GetMapping(URL + "account/update")
    public String updateAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "account/create/retail/confirm")
    public String getCreateRetailAccountConfirmPage(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "discounts/create")
    public String getCreateDiscountPage() {
        return null;
    }

    @GetMapping(URL + "account/create/get")
    public String getCreateAccount() {
        return null;
    }

    @GetMapping(URL + "backup/restore")
    public String getRestoreBackupPage() {
        return null;
    }

    @GetMapping(URL + "orders/history")
    public String getOrderHistoryPage() {
        return null;
    }

    @GetMapping(URL + "orders/invoice/{id}")
    public Invoice getInvoice(@PathVariable Long id) {
        return null;
    }

}
