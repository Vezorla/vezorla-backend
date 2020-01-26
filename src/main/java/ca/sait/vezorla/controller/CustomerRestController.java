package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class CustomerRestController {

    private final String URL = "/customer/";

    @GetMapping(URL + "cart/get")
    public Cart getCart() {
        return null;
    }

    @GetMapping(URL + "cart/update/{id}/{quantity}")
    public void updateCart(@PathVariable Long id, @PathVariable int quantity) {

    }

    @GetMapping(URL + "cart/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {

    }

    @GetMapping(URL + "subscribe/{email}")
    public void subscribeEmail(@PathVariable String email) {

    }

    @GetMapping(URL + "contact")
    public void contactBusiness(String sender, String message) {

    }

    @GetMapping(URL + "cart/update/{id}")
    public void updateCart(@PathVariable Long id) {

    }

    @GetMapping(URL + "account/find/{id}")
    public Account findAccountById(@PathVariable Long id) {
        return null;
    }

    @GetMapping(URL + "discounts/valid/get")
    public List<Discount> getValidDiscounts(Date date) {
        return null;
    }

    @GetMapping(URL + "discounts/apply")
    public boolean applyDiscount(Discount discount) {
        return false;
    }

    @GetMapping(URL + "account/forgotpassword/{email}")
    public void forgotPassword(@PathVariable String email) {

    }

    @GetMapping(URL + "inventory/products/all")
    public List<Product> getAllProducts() {
        return null;
    }

}
