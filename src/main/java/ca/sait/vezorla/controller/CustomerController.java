package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.LineItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(CustomerController.URL)
public class CustomerController {

    protected static final String URL = "/customer/";

    @GetMapping("register")
    public String getRegistrationPage() {
        return null;
    }

    @GetMapping("shop")
    public String getShopPage() {
        return "shopCustomerClientPage";
    }

    @GetMapping("cart")
    public String getCartPage() {
        return null;
    }

    @GetMapping("checkout")
    public String getCheckoutPage() {
        return null;
    }

    @GetMapping("subscription/confirm")
    public String getSubscriptionConfirmationPage(String email) {
        return null;
    }

    @GetMapping("account/create")
    public String createAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("account/forgotpassword")
    public String forgotPassword(String email, Model model) {
        return null;
    }

    @GetMapping("order/place")
    public void placeOrder() {

    }

    @GetMapping("inventory/products")
    public String getProductPage(Long id, Model model) {
        return null;
    }

    @GetMapping("cart/lineitem/create")
    public void createLineItem(LineItem lineItem, Model model, BindingResult bindingResult) {

    }

    @GetMapping("cart/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {

    }

}
