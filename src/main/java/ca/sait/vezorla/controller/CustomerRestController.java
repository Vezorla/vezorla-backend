package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.repository.DiscountRepo;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Rest controller to handle customer functionality
 *
 * @author matthewjflee, jjrr1717
 */
@CrossOrigin
@RestController
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    private UserServices userServices;
    private CartRepo cartRepo;
    private AccountRepo accountRepo;
    private DiscountRepo discountRepo;
    //    @Autowired
    private ObjectMapper mapper;

//    private Account currentAccount;


    public CustomerRestController(UserServices userServices, CartRepo cartRepo, AccountRepo accountRepo, DiscountRepo discountRepo, ObjectMapper mapper) {
        this.userServices = userServices;
        this.cartRepo = cartRepo;
        this.accountRepo = accountRepo;
        this.discountRepo = discountRepo;
        this.mapper = mapper;
    }

    /**
     * Get all products
     *
     * @return List of all products
     * @author kwistech
     */
    @GetMapping("inventory/products/all")
    public List<Product> getAllProducts() {
        return userServices.getAllProducts();
    }

    /**
     * Return the product from Products repo
     *
     * @param id
     * @return
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("inventory/product/{id}")
    public ResponseEntity<Product> getProductPage(@PathVariable Long id) {
        Optional<Product> product = userServices.getProduct(id);

        return product.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Get the total number of products in the cart for a customer
     *
     * @param session
     * @return
     * @author matthewjflee, jjrr1717
     */
    @RequestMapping(value = "cart/get", method = RequestMethod.GET,
            produces = {"application/json"})
    public String getSessionCartQuantity(HttpSession session) {
        Cart cart = userServices.getSessionCart(session);
        return userServices.getTotalSessionCartQuantity((ArrayList<LineItem>) cart.getLineItems());
    }

    /**
     * Return the quantity for the specified product
     *
     * @param id
     * @return
     * @author jjrr1717, matthewjflee
     */
    @RequestMapping(value = "inventory/product/quantity/{id}", method = RequestMethod.GET, produces = {"application/json"})
    public int getProductQuantity(@PathVariable Long id) {
        return userServices.getProductQuantity(id);
    }

    /**
     * Create a line item in the cart for a customer
     *
     * @param id
     * @param quantity
     * @param request
     * @return
     * @author matthewjflee, jjrr1717
     */
    @RequestMapping(value = "cart/add/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    public boolean createLineItemSession(@PathVariable Long id, @RequestBody String quantity, HttpServletRequest request) {

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

    /**
     * View cart for a customer
     *
     * @param session
     * @return
     * @throws JsonProcessingException
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("cart/view")
    public String viewSessionCart(HttpSession session) throws JsonProcessingException {
        ArrayNode arrayNode = userServices.viewSessionCart(session);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }

    /**
     * Update a line item in the cart for a customer
     *
     * @param id
     * @param quantity
     * @param request
     * @return
     * @throws JsonProcessingException
     * @author matthewjflee, jjrr1717
     */
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

    /**
     * Remove a line item for a customer
     *
     * @param id
     * @param request
     * @return
     * @throws JsonProcessingException
     * @author matthewjflee, jjrr1717
     */
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
     * @author matthewjflee, jjrr1717
     */
    @RequestMapping(value = "/cart/checkout/shipping", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public boolean getShippingInfo(HttpEntity<String> httpEntity, HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean created = false;
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
//            currentAccount = new Account(email, lastName, firstName, phoneNumber, address, city, country, postalCode);

            session.setAttribute("ACCOUNT", newAccount);
            created = true;
//            created = userServices.saveAccount(currentAccount);
        } catch (ParseException e) {
        }

        return created;
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

    /**
     * Return all valid discounts associated with the customer/client
     * This method will query the database for all valid discounts for the account
     *
     * @return
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("discounts/get")
    public String getValidDiscounts(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        Account currentAccount = (Account) session.getAttribute("ACCOUNT");

        ArrayList<Discount> discounts = (ArrayList<Discount>) userServices.getValidDiscounts(currentAccount.getEmail());
        ArrayNode arrayNode = mapper.createArrayNode();

        for (int i = 0; i < discounts.size(); i++) {
            ObjectNode node = mapper.createObjectNode();
            node.put("code", discounts.get(i).getCode());
            node.put("description", discounts.get(i).getDescription());
            node.put("percent", discounts.get(i).getPercent());

            arrayNode.add(node);
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
//
    }

    /**
     * Get the discount code when a user
     * selects the discount and save it
     * to the session. Will be persisted
     * into the database at time of checkout,
     * rather than here.
     * @param code discount code the user selected
     * @param request for the session
     */
    @GetMapping("selected_discount/get")
    public void getSelectedDiscount(@RequestBody String code, HttpServletRequest request){
        HttpSession session = request.getSession();
        userServices.getSelectedDiscount(code, request, session);
    }

    /**
     * Show details of order on the
     * review page
     * @param session
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("cart/review")
    public String reviewOrder(HttpSession session)throws JsonProcessingException{

        Cart cart = (Cart) session.getAttribute("CART");
        ObjectNode root = mapper.createObjectNode();
        ObjectNode nodeItem = mapper.createObjectNode();
        ObjectNode node = mapper.createObjectNode();
        ArrayNode itemsArrayNode = mapper.createArrayNode();
        ArrayNode mainArrayNode = mapper.createArrayNode();
        long subtotal = 0;
        final float TAX_RATE = 0.05f;
        final long SHIPPING_RATE = 1000;

        for (int i = 0; i < cart.getLineItems().size(); i++) {
            int quantity = cart.getLineItems().get(i).getQuantity();
            long price = cart.getLineItems().get(i).getProduct().getPrice();

            nodeItem.put("name", cart.getLineItems().get(i).getProduct().getName());
            nodeItem.put("quantity", quantity);
            nodeItem.put("price", userServices.formatAmount(price));
            //get over the extended price
            long extendedPrice = price * quantity;

            nodeItem.put("extendedPrice", userServices.formatAmount(extendedPrice));
            //get subtotal
            subtotal += extendedPrice;

            itemsArrayNode.add(nodeItem);
        }

        node.put("subtotal", userServices.formatAmount(subtotal));

        //get discount
        long discountAmount;
        AccountDiscount discountType = (AccountDiscount) session.getAttribute("ACCOUNT_DISCOUNT");
        if(discountType != null) {
            float discountPercent = Float.parseFloat(discountRepo.findDiscountPercent(discountType.getCode().getCode()));
            long discountDecimal = (long) discountPercent / 100;

            discountAmount = subtotal * discountDecimal;
        }
        else{
            discountAmount = 0;
        }
        node.put("discount", userServices.formatAmount(discountAmount));

        //discounted subtotal
        long discountedSubtotal = subtotal - discountAmount;
        node.put("discounted_subtotal", userServices.formatAmount(discountedSubtotal));

        //calculate Taxes
        long taxes = (long) (discountedSubtotal * TAX_RATE);
        node.put("taxes", userServices.formatAmount(taxes));

        //flat shipping rate
        node.put("shipping", userServices.formatAmount(SHIPPING_RATE));

        //calculate total
        long total = discountedSubtotal + taxes + SHIPPING_RATE;
        node.put("Total", userServices.formatAmount(total));

        mainArrayNode.add(itemsArrayNode);
        mainArrayNode.add(node);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mainArrayNode);

    }

    /**
     * Apply discount to the cart
     *
     * @return
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("discounts/apply")
    public boolean applyDiscount() {

        return false;
    }

    @GetMapping("account/forgotpassword/{email}")
    public void forgotPassword(@PathVariable String email) {

    }
}