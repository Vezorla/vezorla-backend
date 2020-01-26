package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.LineItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CustomerController {

    private final String URL = "/customer/";

    @GetMapping(URL + "register")
    public String getRegistrationPage() {
        return null;
    }

    @GetMapping(URL + "cart")
    public String getCartPage() {
        return null;
    }

    @GetMapping(URL + "checkout")
    public String getCheckoutPage() {
        return null;
    }

    @GetMapping(URL + "subscription/confirm")
    public String getSubscriptionConfirmationPage(String email) {
        return null;
    }

    @GetMapping(URL + "account/create")
    public String createAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "account/forgotpassword")
    public String forgotPassword(String email, Model model) {
        return null;
    }

    @GetMapping(URL + "order/place")
    public void placeOrder() {

    }

    @GetMapping(URL + "inventory/products")
    public String getProductPage(Long id, Model model) {
        return null;
    }

    @GetMapping(URL + "cart/lineitem/create")
    public void createLineItem(LineItem lineItem, Model model, BindingResult bindingResult) {

    }

    @GetMapping(URL + "cart/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {

    }

}
