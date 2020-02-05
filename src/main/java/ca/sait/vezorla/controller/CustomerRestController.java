package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    @Autowired
    private final UserServices userServices;

    public CustomerRestController(UserServices userServices) {
        this.userServices = userServices;
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

    ///////////////////////////////////////////////

    // EXAMPLE CODE FOR CART QUANTITY
    @GetMapping("quantity")
    public Map<String, Integer> quantity() {
        Hello h = new Hello();
        return Collections.singletonMap("quantity", h.getHi().size());
    }

    // -------------------------------

    @GetMapping("inventory/products/all")
    public List<Product> getAllProducts() {
        return userServices.getAllProducts();
    }

}

class Hello {
    ArrayList<Integer> hi;
    public Hello() {
        hi = new ArrayList<>();
        hi.add(1);
        hi.add(2);
        hi.add(3);
        hi.add(4);
    }
    public ArrayList<Integer> getHi() {
        return hi;
    }
}