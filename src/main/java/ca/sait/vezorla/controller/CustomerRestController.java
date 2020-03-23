
package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.PasswordMismatchException;
import ca.sait.vezorla.exception.UnableToSaveException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.LineItem;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.EmailServices;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
@AllArgsConstructor
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";
    private UserServices userServices;
    private EmailServices emailServices;

    /**
     * Get all products
     *
     * @return List of all products
     * @author kwistech
     */
    @GetMapping("inventory/products/all")
    public String getAllProducts() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getAllProducts(mapper));
    }

    /**
     * Return the product from Products repo
     *
     * @param id
     * @return
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("inventory/product/{id}")
    public String getProductPage(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getProduct(id, mapper));

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
    public String createLineItemSession(@PathVariable Long id, @RequestBody String quantity, HttpServletRequest request) throws JsonProcessingException {
        ArrayList<LineItem> lineItems;
        HttpSession session = request.getSession();
        Cart cart = userServices.getSessionCart(session);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        Optional<Product> product = userServices.getProduct(id);
        int productInStock = userServices.getProductQuantity(id);
        int checkProductStock = userServices.validateOrderedQuantity(quantity, productInStock);

        if (checkProductStock >= 0) {
            lineItems = userServices.createLineItemSession(product, quantity, cart);

            if(!lineItems.isEmpty()) {
                userServices.updateSessionCart(lineItems, cart, request);
                node.put("added", true);
            }
        } else {
            node.put("added", false);
            node.put("currentStock", productInStock);
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    /**
     * View cart for a customer
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("cart/view")
    public String viewSessionCart(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Cart cart = (Cart) session.getAttribute("CART");
        ArrayNode arrayNode = userServices.viewSessionCart(request, cart);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }

    /**
     * Method to obtain items out of stock that
     * is on an order.
     * Must be requested before viewing the order
     * and payment.
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("cart/view/out_of_stock")
    public String viewItemsOutOfStock(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Cart cart = (Cart) session.getAttribute("CART");
        ArrayNode outOfStockItems = userServices.checkItemsOrderedOutOfStock(cart, request);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outOfStockItems);
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
        return userServices.getTotalCartQuantity((ArrayList<LineItem>) cart.getLineItems());
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

        return result;
    }

    /**
     * Obtain customer's shipping information from front end
     *
     * @author matthewjflee, jjrr1717
     */
    @RequestMapping(value = "/cart/checkout/shipping",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<String> getShippingInfo(@RequestBody Account account,
                                                  HttpServletRequest request)
            throws JsonProcessingException, InvalidInputException, UnauthorizedException {
        String output;
        HttpSession session = request.getSession();

        if (session.getAttribute("CART") != null)
            output = userServices.getShippingInfo(session, account);
        else
            throw new UnauthorizedException();

        return ResponseEntity.ok().body(output);
    }

    /**
     * Create a new account
     *
     * @param body: JSON sending email and password
     * @author: matthewjflee
     */
    @PostMapping("create-account")
    public boolean createAccount(@RequestBody String body, HttpServletRequest request) {
        boolean created = false;
        String email = null;
        String password = null;
        String rePassword = null;
        HttpSession session = request.getSession();

        try {
            Object obj = new JSONParser().parse(body);
            JSONObject jo = (JSONObject) obj;
            email = (String) jo.get("email");
            password = (String) jo.get("password");
            rePassword = (String) jo.get("rePassword");
        } catch (ParseException e) {
        }

        //Check if password and rePassword are the same
        assert password != null;
        if (!password.equals(rePassword))
            throw new PasswordMismatchException();

        //Check if account exists
        Optional<Account> newAccount = userServices.findAccountByEmail(email);
        if (newAccount.isPresent()) //Account exists.
            return created;
        else {
            newAccount = Optional.of(new Account(email, password));
            created = userServices.saveAccount(newAccount.get());
            if (!created)
                throw new UnableToSaveException();
            else
                session.setAttribute("ACCOUNT", newAccount.get());
        }

        return created;
    }

    /**
     * Subscribe user to the mailing list
     * If the user's account does not exist, create a new account and save to the Accounts table
     *
     * @param email
     * @author: matthewjflee
     */
    @PostMapping("subscribe")
    public boolean subscribeEmail(@RequestBody String email) throws InvalidInputException {
        String replaceEmail = email.replaceAll("\"", "");
        emailServices.verifyEmail(replaceEmail);
        Account account = userServices.findAccountByEmail(replaceEmail).orElse(new Account(replaceEmail));
        account.setSubscript(true);
        boolean save = userServices.saveAccount(account);
        if (!save)
            throw new UnableToSaveException();

        return true;
    }

    /**
     * Return all valid discounts associated with the customer/client
     * This method will query the database for all valid discounts for the account
     *
     * @return
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("discounts/get")
    public String getValidDiscounts(HttpServletRequest request) throws JsonProcessingException, UnauthorizedException {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession();
        ArrayNode arrayNode = mapper.createArrayNode();
        if (session.getAttribute("ACCOUNT") != null) {
            arrayNode = userServices.buildValidDiscounts(session, arrayNode);
        } else {
            throw new UnauthorizedException();
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }

    /**
     * Get the discount code when a user
     * selects the discount and save it
     * to the session. Will be persisted
     * into the database at time of checkout,
     * rather than here.
     *
     * @param code    discount code the user selected
     * @param request for the session
     */
    @GetMapping("selected_discount/get")
    public void getSelectedDiscount(@RequestBody String code, HttpServletRequest request) throws UnauthorizedException {
        HttpSession session = request.getSession();
        userServices.getSelectedDiscount(code, request, session);
    }

    /**
     * Show details of order on the
     * review page
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("cart/review")
    public String reviewOrder(HttpServletRequest request) throws JsonProcessingException, UnauthorizedException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Cart cart = (Cart) session.getAttribute("CART");
        ArrayNode mainArrayNode = mapper.createArrayNode();
        ArrayNode outOfStockItems = mapper.createArrayNode();
        if (session.getAttribute("ACCOUNT_DISCOUNT") != null) {
            //outOfStockItems = userServices.checkItemsOrderedOutOfStock(cart, request);
            mainArrayNode = userServices.reviewOrder(session, mainArrayNode, cart);
            //mainArrayNode.add(outOfStockItems);
        } else {
            throw new UnauthorizedException();
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mainArrayNode);
    }

    /**
     * Send contact us email
     * Returns <code>true</code> if email is sent
     * <code>false</code> if email fails to send
     *
     * @param body
     * @author: matthewjflee
     */
    @PostMapping("contact-us")
    public boolean contactBusiness(@RequestBody String body) throws InvalidInputException {
        String name, senderEmail, message;

        //Parse request
        try {
            Object obj = new JSONParser().parse(body);
            JSONObject jo = (JSONObject) obj;
            name = (String) jo.get("name");
            senderEmail = (String) jo.get("senderEmail");
            message = (String) jo.get("message");
        } catch (ParseException e) {
            return false;
        }

        //Send email
        if (name != null && senderEmail != null && message != null) {
            try {
                emailServices.sendContactUsEmail(name, senderEmail, message);
            } catch (MailException e) {
                return false;
            }

            System.out.println("Email sent!");
            return true;
        }

        return false;
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
}