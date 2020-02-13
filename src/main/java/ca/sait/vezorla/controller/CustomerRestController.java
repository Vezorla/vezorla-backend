package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    private UserServices userServices;
    private CartRepo cartRepo;
//    private ArrayList<LineItem> lineItems = new ArrayList<>();
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private HttpSession session;


    public CustomerRestController(UserServices userServices, CartRepo cartRepo) {
        this.userServices = userServices;
        this.cartRepo = cartRepo;
    }

    @GetMapping("inventory/product/{id}")
    public ResponseEntity<Product> getProductPage(@PathVariable Long id) { //Changed to ResponseEntity
        Optional<Product> product = userServices.getProduct(id);

        return product.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @RequestMapping(value = "inventory/product/quantity/{id}", method = RequestMethod.PUT,
//            produces = {"application/json"})
//    public int getProductQuantity(@PathVariable Long id) { //Changed to ResponseEntity
//        return userServices.getProductQuantity(id);
//    }

    @RequestMapping(value = "cart/get", method = RequestMethod.GET,
            produces = {"application/json"})
    public String getSessionCartQuantity() {
//        userServices.createSessionCart();
        Cart cart = userServices.getSessionCart(session); //TODO Issue
        System.out.println(cart.toString());
//        cart.setLineItems(lineItems);
        System.out.println("Line Item List Size" + cart.getLineItems().size());
        return userServices.getTotalSessionCartQuantity((ArrayList<LineItem>) cart.getLineItems()); //cart.getLineItems().size() + "";
    }

    @RequestMapping(value = "inventory/product/quantity/{id}", method = RequestMethod.GET,
            produces = {"application/json"})
    public int getProductQuantity(@PathVariable Long id) {
        return userServices.getProductQuantity(id);
    }

    //@PutMapping("cart/get")
    public void updateSessionCart(LineItem lineItem) { //used ResponseEntity<> so backend handles errors
//        Cart result = userServices.updateSessionCart(lineItem);
        //return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "cart/add/{id}", method = RequestMethod.PUT,
            produces = {"application/json"})
    public boolean createLineItem(@PathVariable Long id, @RequestBody String quantity) throws JsonProcessingException {

        LineItem lineItem = null;
        boolean result = false;
        Optional<Product> product = userServices.getProduct(id);
//        System.out.println("S" + quantity);
        int productInStock = userServices.getProductQuantity(id);
//        System.out.println("prod in stock" + productInStock);
        int checkProductStock = userServices.validateOrderedQuantity(quantity, productInStock);
//        System.out.println("check me" + checkProductStock);

        if(checkProductStock >= 0) {
            lineItem = userServices.createLineItemSession(product, quantity, session);
        }

        if (lineItem != null) {
            userServices.updateSessionCart(lineItem, session);
            result = true;
        }

        return result;
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
        return userServices.getAllProducts();
    }
}