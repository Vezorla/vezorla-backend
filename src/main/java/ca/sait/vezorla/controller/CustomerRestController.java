package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    private UserServices userServices;

    public CustomerRestController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("inventory/product/{id}")
    public ResponseEntity<Product> getProductPage(@PathVariable Long id) { //Changed to ResponseEntity
        Optional<Product> product = userServices.getProduct(id);

        return product.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("cart/get")
    public Cart getCart() {
        return null;
    }

    @GetMapping("cart/update/{id}/{quantity}")
    public void updateCart(@PathVariable Long id, @PathVariable int quantity) {

    }

    @GetMapping("cart/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {

    }

    @GetMapping("subscribe/{email}")
    public void subscribeEmail(@PathVariable String email) {

    }

    @GetMapping("contact")
    public void contactBusiness(String sender, String message) {

    }

    @GetMapping("cart/update/{id}")
    public void updateCart(@PathVariable Long id) {

    }

    @GetMapping("account/find/{id}")
    public Account findAccountById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("discounts/valid/get")
    public List<Discount> getValidDiscounts(Date date) {
        return null;
    }

    @GetMapping("discounts/apply")
    public boolean applyDiscount(Discount discount) {
        return false;
    }

    @GetMapping("account/forgotpassword/{email}")
    public void forgotPassword(@PathVariable String email) {

    }

    @GetMapping("inventory/products/all")
    public List<Product> getAllProducts() {
        return null;
    }

}
