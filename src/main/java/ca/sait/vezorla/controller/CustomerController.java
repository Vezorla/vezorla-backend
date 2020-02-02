package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.LineItem;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.repository.ProductRepo;
import ca.sait.vezorla.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.Response;
import java.util.Optional;

@Controller
@RequestMapping(CustomerController.URL)
public class CustomerController {

    @Autowired
    private UserServices userServices;

    protected static final String URL = "/customer/";

    @GetMapping("register")
    public String getRegistrationPage() {
        return null;
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

    @GetMapping("inventory/product/{id}")
    public ResponseEntity<Product> getProductPage(Long id, Model model) { //Changed to ResponseEntity
        Optional<Product> product = userServices.getProduct(id);

        return product.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("cart/lineitem/create")
    public void createLineItem(LineItem lineItem, Model model, BindingResult bindingResult) {

    }

    @GetMapping("cart/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {

    }

}
