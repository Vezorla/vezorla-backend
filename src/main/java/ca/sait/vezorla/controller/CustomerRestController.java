package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    private UserServices userServices;
    private CartRepo cartRepo;
    ArrayList<LineItem> lineItems = new ArrayList<>();


    public CustomerRestController(UserServices userServices, CartRepo cartRepo) {
        this.userServices = userServices;
        this.cartRepo = cartRepo;
    }

    @GetMapping("inventory/product/{id}")
    public ResponseEntity<Product> getProductPage(@PathVariable Long id) { //Changed to ResponseEntity
        Optional<Product> product = userServices.getProduct(id);

        return product.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


//    @GetMapping("cart/get")
//    public ResponseEntity<Cart> getSessionCart() {
//        Optional<Cart> cart = Optional.ofNullable(userServices.getSessionCart());
//
//        return cart.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    @GetMapping("cart/get")
    public Map<String, String> getSessionCart() {
        userServices.createSessionCart();
        Cart cart = userServices.getSessionCart();
//       LineItem test = new LineItem();
//       ArrayList<LineItem> testList = new ArrayList<>();
//      testList.add(test);
        cart.setLineItems(lineItems);
        String quantity = cart.getLineItems().size() + "";
//       cart.setLineItems(testList);
        return Collections.singletonMap("quantity", quantity);
    }

    @PutMapping("cart/get")
    public ResponseEntity<?> updateCart(@Valid @RequestBody LineItem lineItem) { //used ResponseEntity<> so backend handles errors
        Cart result = userServices.updateSessionCart(lineItem);

        return ResponseEntity.ok().body(result);
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
