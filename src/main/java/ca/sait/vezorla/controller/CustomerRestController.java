package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    private UserServices userServices;
    private CartRepo cartRepo;
    private AccountRepo accountRepo;

    @Autowired
    ObjectMapper mapper;

    public CustomerRestController(UserServices userServices, CartRepo cartRepo, AccountRepo accountRepo) {
        this.userServices = userServices;
        this.cartRepo = cartRepo;
        this.accountRepo = accountRepo;
    }

    /**
     * Get all products
     *
     * @return List of all products
     */
    @GetMapping("inventory/products/all")
    public List<Product> getAllProducts() {
        return userServices.getAllProducts();
    }

    @GetMapping("inventory/product/{id}")
    public ResponseEntity<Product> getProductPage(@PathVariable Long id) {
        Optional<Product> product = userServices.getProduct(id);

        return product.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "cart/get", method = RequestMethod.GET,
            produces = {"application/json"})
    public String getSessionCartQuantity(HttpSession session) {
        Cart cart = userServices.getSessionCart(session);
        return userServices.getTotalSessionCartQuantity((ArrayList<LineItem>) cart.getLineItems());
    }

    @RequestMapping(value = "inventory/product/quantity/{id}", method = RequestMethod.GET, produces = {"application/json"})
    public int getProductQuantity(@PathVariable Long id) {
        return userServices.getProductQuantity(id);
    }

    @RequestMapping(value = "cart/add/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    public boolean createLineItem(@PathVariable Long id, @RequestBody String quantity, HttpServletRequest request) {

        System.out.println("sent quantity: " + quantity);

        LineItem lineItem = null;
        boolean result = false;
        Optional<Product> product = userServices.getProduct(id);
        int productInStock = userServices.getProductQuantity(id);
        int checkProductStock = userServices.validateOrderedQuantity(quantity, productInStock);

        if (checkProductStock >= 0)
            lineItem = userServices.createLineItemSession(product, quantity, request);

        if (lineItem != null) {
            userServices.updateSessionCart(lineItem, request);
            result = true;
        }

        return result;
    }

    @GetMapping("cart/view")
    public String viewSessionCart(HttpSession session) throws JsonProcessingException {
        Cart cart = (Cart) session.getAttribute("CART");
//        ObjectNode root = mapper.createObjectNode();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode));

        for (int i = 0; i < cart.getLineItems().size(); i++) {
            ObjectNode node = mapper.createObjectNode();
            node.put("prodID", cart.getLineItems().get(i).getProduct().getProdId());
            node.put("name", cart.getLineItems().get(i).getProduct().getName());
            node.put("price", cart.getLineItems().get(i).getProduct().getPrice());
            node.put("imageMain", cart.getLineItems().get(i).getProduct().getImageMain());
            node.put("quantity", cart.getLineItems().get(i).getQuantity());
            arrayNode.add(node);
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }


    @PutMapping("cart/update/{id}/{quantity}")
    public boolean updateLineItemSession(@PathVariable Long id, @PathVariable int quantity, HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        Cart cart = userServices.getSessionCart(session);
        boolean result = userServices.updateLineItemSession(id, quantity, cart, request);
        if (result) {
            viewSessionCart(session);
        }

        return result;
    }

    @PutMapping("cart/remove/{id}")
    public boolean removeLineItemSession(@PathVariable Long id, HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        Cart cart = userServices.getSessionCart(session);
        boolean result = userServices.removeLineItemSession(id, cart, request);
        if (result) {
            viewSessionCart(session);
        }

        return result;
    }

    /**
     * Obtain customer's shipping information from front end
     *
     * @param httpEntity
     */
    @RequestMapping(value = "/cart/checkout/shipping", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public void getShippingInfo(HttpEntity<String> httpEntity) {
        String json = httpEntity.getBody();
        try {
            Object obj = new JSONParser().parse(json);
            JSONObject jo = (JSONObject) obj;
            String email = (String) jo.get("email");
            String firstName = (String) jo.get("firstName");
            String lastName = (String) jo.get("lastName");
            String phoneNumber = (String) jo.get("phoneNumber");
            String address = (String) jo.get("address");
            String city = (String) jo.get("city");
            String country = (String) jo.get("country");
            String postalCode = (String) jo.get("postalCode");

            Account newAccount = new Account(email, lastName, firstName, phoneNumber, address, city, country, postalCode);

            boolean created = userServices.saveAccount(newAccount);
        } catch (ParseException e) {}
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

        userServices.getValidDiscounts();

        return false;
    }

    @GetMapping("account/forgotpassword/{email}")
    public void forgotPassword(@PathVariable String email) {

    }
}