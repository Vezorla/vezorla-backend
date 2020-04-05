
package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.*;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.LineItem;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.AccountServices;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private AccountServices accountServices;

    /**
     * Get all products
     *
     * @return List of all products
     * @author jjrr1717
     */
    @GetMapping("inventory/products/all")
    public String getAllProducts() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getAllProducts(mapper));
    }

    /**
     * Return the product from Products repo
     *
     * @param id ID of product
     * @return product
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
     * @param id product ID
     * @return product's quantity
     * @author jjrr1717, matthewjflee
     */
    @RequestMapping(value = "inventory/product/quantity/{id}", method = RequestMethod.GET, produces = {"application/json"})
    public int getProductQuantity(@PathVariable Long id) {
        return userServices.getProductQuantity(id);
    }

    /**
     * Create a line item in the cart for a customer
     *
     * @param id       product id to add to a cart
     * @param quantity quantity to add
     * @param request  user's request
     * @return line item created
     * @author matthewjflee, jjrr1717
     */
    @RequestMapping(value = "cart/add/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    public String createLineItem(@PathVariable Long id, @RequestBody int quantity, HttpServletRequest request) throws JsonProcessingException {
        List<LineItem> lineItems;
        HttpSession session = request.getSession();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        Account account = (Account) session.getAttribute("ACCOUNT");
        Cart cart = userServices.getCart(session);

        //Validate product quantity
        Optional<Product> findProduct = userServices.getProduct(id);
        int productInStock = userServices.getProductQuantity(id);
        int checkProductStock = userServices.validateOrderedQuantity(quantity, productInStock);

        if (checkProductStock >= 0) {
            if (findProduct.isPresent()) {
                Product product = findProduct.get();
                lineItems = userServices.createLineItem(product, quantity, cart);

                if (!lineItems.isEmpty()) {

                    if (account != null && account.isUserCreated()) {
                        accountServices.saveAccount(account, session);
                        accountServices.saveCart(cart);
                    } else
                        userServices.addLineItemToSessionCart(lineItems, cart, session);

                    node.put("added", true);
                }
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
     * @param request user's request
     * @return user's cart
     * @throws JsonProcessingException error when parsing the JSON
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("cart/view")
    public String viewCart(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Cart cart = userServices.getCart(session);

        ArrayNode arrayNode = userServices.viewCart(cart);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }

    /**
     * Method to obtain items out of stock that
     * is on an order.
     * Must be requested before viewing the order
     * and payment.
     *
     * @param request user session
     * @return out of stock items
     * @throws JsonProcessingException exception thrown when creating a JSON
     * @author jjrr1717
     */
    @GetMapping("cart/view/out_of_stock")
    public String viewItemsOutOfStock(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Cart cart = userServices.getCart(session);

        ArrayNode outOfStockItems = userServices.checkItemsOrderedOutOfStock(cart, session);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outOfStockItems);
    }

    /**
     * Get the total number of products in the cart for a customer
     *
     * @param session user's session
     * @return JSON of user's cart
     * @author matthewjflee, jjrr1717
     */
    @RequestMapping(value = "cart/get", method = RequestMethod.GET,
            produces = {"application/json"})
    public String getCartQuantity(HttpSession session) {
        Cart cart = userServices.getCart(session);
        return userServices.getTotalCartQuantity(cart.getLineItems());
    }

    /**
     * Update a line item in the cart for a customer
     *
     * @param id       Line item ID
     * @param quantity quantity to change
     * @param request  user's request
     * @return boolean if it was changed or not
     * @author matthewjflee, jjrr1717
     */
    @PutMapping("cart/update/{id}/{quantity}")
    public boolean updateLineItem(@PathVariable Long id, @PathVariable int quantity, HttpServletRequest request)  {
        HttpSession session = request.getSession();
        Cart cart = userServices.getCart(session);

        return userServices.updateLineItem(id, quantity, cart, session);
    }

    /**
     * Remove a line item for a customer
     *
     * @param id      line item to delete
     * @param request user's request
     * @return if line item was deleted or not
     * @author matthewjflee, jjrr1717
     */
    @DeleteMapping("cart/remove/{id}")
    public boolean removeLineItem(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart = userServices.getCart(session);

        return userServices.removeLineItem(id, cart, session);
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
    public ResponseEntity<String> getShippingInfo(@RequestBody Account sendAccount,
                                                  HttpServletRequest request)
            throws JsonProcessingException, InvalidInputException, UnauthorizedException {
        HttpSession session = request.getSession();
        String output;
        Account sessionAccount = (Account) session.getAttribute("ACCOUNT");
        Cart cart = userServices.getCart(session);

        //Grab account
        if (sessionAccount != null) {
            Optional<Account> findAccount = accountServices.findAccountByEmail(sessionAccount.getEmail());
            if (findAccount.isPresent()) {
                Account updateAccount = findAccount.get();
                sendAccount = accountServices.updateAccount(updateAccount, sendAccount);
            }
        }

        //Check
        if (cart.getLineItems().size() > 0)
            output = userServices.getShippingInfo(session, sendAccount);
        else
            throw new UnauthorizedException();

        return ResponseEntity.ok().body(output);
    }

    /**
     * Return the user's info and check if user's line items are out of stock
     * @param request user request
     * @return user information
     * @throws JsonProcessingException error parsing JSON
     * @throws OutOfStockException error if the line item is out of stock
     */
    @GetMapping(value = "info")
    public String getUserInfo(HttpServletRequest request) throws JsonProcessingException, OutOfStockException {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession();
        Account sessionAccount = (Account) session.getAttribute("ACCOUNT");
        Account account = null;

        if (sessionAccount != null) {
            Optional<Account> findAccount = accountServices.findAccountByEmail(sessionAccount.getEmail());
            if (findAccount.isPresent())
                account = findAccount.get();
        } else
            throw new AccountNotFoundException();

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getUserInfo(account, mapper));
    }

    /**
     * Check if the cart is empty or if a line item is out of stock
     * @param request user session
     * @return <code>true</code> if there no line items are out of stock
     * @throws OutOfStockException thrown if a line item is out of stock
     * @author matthewjflee
     */
    @GetMapping(value = "cart/check-out-of-stock")
    public boolean checkCartAllItemsOutOfStock(HttpServletRequest request) throws OutOfStockException {
        HttpSession session = request.getSession();
        Cart cart = userServices.getCart(session);

        if (!userServices.checkLineItemStock(cart))
            throw new OutOfStockException();

        return true;
    }

    /**
     * Subscribe user to the mailing list
     * If the user's account does not exist, create a new account and save to the Accounts table
     *
     * @param email user's email to subscribe
     * @author matthewjflee
     */
    @PostMapping("subscribe")
    public boolean subscribeEmail(@RequestBody String email) throws InvalidInputException {
        String replaceEmail = email.replaceAll("\"", "");
        emailServices.verifyEmail(replaceEmail);
        Account account = accountServices.findAccountByEmail(replaceEmail).orElse(new Account(replaceEmail));
        account.setIsSubscript(true);
        boolean save = accountServices.saveAccount(account);
        if (!save)
            throw new UnableToSaveException();

        return true;
    }

    /**
     * Return all valid discounts associated with the customer/client
     * This method will query the database for all valid discounts for the account
     *
     * @return user's valid discounts
     * @author matthewjflee, jjrr1717
     */
    @GetMapping("discounts/get")
    public String getValidDiscounts(HttpServletRequest request) throws JsonProcessingException, UnauthorizedException {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession();
        ArrayNode arrayNode = mapper.createArrayNode();

        if (session.getAttribute("ACCOUNT") != null)
            arrayNode = userServices.buildValidDiscounts(session, arrayNode);
        else
            throw new UnauthorizedException();

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
     * @author jjrr1717
     */
    @PutMapping("selected_discount/get")
    public void getSelectedDiscount(@RequestBody String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        userServices.getSelectedDiscount(code, session);
    }

    /**
     * Show details of order on the
     * review page
     *
     * @param request user request
     * @return user's order
     * @throws JsonProcessingException parsing error
     * @author jjrr1717
     */
    @GetMapping("cart/review")
    public String reviewOrder(HttpServletRequest request) throws JsonProcessingException, UnauthorizedException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Cart cart = userServices.getCart(session);
        ArrayNode mainArrayNode = mapper.createArrayNode();

        if (session.getAttribute("ACCOUNT_DISCOUNT") != null)
            mainArrayNode = userServices.reviewOrder(session, mainArrayNode, cart);
        else
            throw new UnauthorizedException();

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mainArrayNode);
    }

    /**
     * Send contact us email
     * Returns <code>true</code> if email is sent
     * <code>false</code> if email fails to send
     *
     * @param body user's email to vezorla
     * @author matthewjflee
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

            return true;
        }

        return false;
    }

    /**
     * Method to complete transactions after a successful payment
     *
     * @param success boolean if payment was successful
     * @param request for the session
     * @throws UnauthorizedException if user is unauthorized
     * @throws InvalidInputException if phone number or postal code is invalid
     * @author jjrr1717
     */
    @PutMapping("payment/success")
    public boolean successfulPayment(@RequestBody boolean success, HttpServletRequest request) throws UnauthorizedException, InvalidInputException {
        boolean output = false;
        if (success) {
            HttpSession session = request.getSession();
            output = userServices.paymentTransactions(session);

        }
        return output;
    }

    /**
     * Method to send banner information for home page
     *
     * @param mapper for custom json
     * @return String of custom json
     * @throws JsonProcessingException
     */
    @GetMapping("banner")
    public String getBannerMessage(ObjectMapper mapper) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getBannerMessage(mapper));
    }

    /**
     * Method to return the top product sold
     * by Vezorla
     *
     * @return mapper for the json
     * @throws JsonProcessingException
     * @author jjrr1717
     */
    @GetMapping("top_product")
    public String getTopProduct() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userServices.getTopProduct(mapper));
    }

}