package ca.sait.vezorla.controller;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.repository.DiscountRepo;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(CustomerRestController.URL)
public class CustomerRestController {

    protected static final String URL = "/api/customer/";

    private UserServices userServices;
    private CartRepo cartRepo;
    private AccountRepo accountRepo;
    private DiscountRepo discountRepo;
    private ObjectMapper mapper;
    private CustomerClientUtil customerClientUtil;


    public CustomerRestController(UserServices userServices,
                                  CartRepo cartRepo,
                                  AccountRepo accountRepo,
                                  DiscountRepo discountRepo,
                                  ObjectMapper mapper) {
        this.userServices = userServices;
        this.cartRepo = cartRepo;
        this.accountRepo = accountRepo;
        this.discountRepo = discountRepo;
        this.mapper = mapper;
        this.customerClientUtil = new CustomerClientUtil();
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
    @RequestMapping(value = "/cart/checkout/shipping",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<String> getShippingInfo(HttpEntity<String> httpEntity,
                                                  HttpServletRequest request)
            throws JsonProcessingException, InvalidInputException {
        String json = httpEntity.getBody();
        String output = userServices.getShippingInfo(httpEntity, request, json);
        if(output.equals("true")){
            HttpSession session = request.getSession();
            request.getSession().setAttribute("CHECKOUT_FLOW_TOKEN", "1");
        }
        return ResponseEntity.ok().body(output);
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
        HttpSession session = request.getSession();
        ArrayNode arrayNode = mapper.createArrayNode();
        if(session.getAttribute("CHECKOUT_FLOW_TOKEN") == null){
            throw new UnauthorizedException();
        }
        else if(!session.getAttribute("CHECKOUT_FLOW_TOKEN").equals("1")){
            throw new UnauthorizedException();
        }
        else{
            Account currentAccount = (Account) session.getAttribute("ACCOUNT");
            ArrayList<Discount> discounts = (ArrayList<Discount>) userServices.getValidDiscounts(currentAccount.getEmail());
            for (int i = 0; i < discounts.size(); i++) {
                ObjectNode node = mapper.createObjectNode();
                node.put("code", discounts.get(i).getCode());
                node.put("description", discounts.get(i).getDescription());
                node.put("percent", discounts.get(i).getPercent());
                arrayNode.add(node);
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
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
    public void getSelectedDiscount(@RequestBody String code, HttpServletRequest request) throws UnauthorizedException {
        HttpSession session = request.getSession();
        userServices.getSelectedDiscount(code, request, session);
        request.getSession().setAttribute("CHECKOUT_FLOW_TOKEN", "2");
    }

    /**
     * Show details of order on the
     * review page
     * @param session
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("cart/review")
    public String reviewOrder(HttpSession session) throws JsonProcessingException, UnauthorizedException {
        ArrayNode mainArrayNode = mapper.createArrayNode();
        if(session.getAttribute("CHECKOUT_FLOW_TOKEN") == null){
            throw new UnauthorizedException();
        }
        else if(!session.getAttribute("CHECKOUT_FLOW_TOKEN").equals("2")){
            throw new UnauthorizedException();
        }
        else {

            Cart cart = (Cart) session.getAttribute("CART");
            ObjectNode root = mapper.createObjectNode();
            ObjectNode nodeItem = mapper.createObjectNode();
            ObjectNode node = mapper.createObjectNode();
            ArrayNode itemsArrayNode = mapper.createArrayNode();


            long subtotal = 0;
            long shippingRate = 1000;
            final float TAX_RATE = 0.05f;

            for (int i = 0; i < cart.getLineItems().size(); i++) {
                int quantity = cart.getLineItems().get(i).getQuantity();
                long price = cart.getLineItems().get(i).getProduct().getPrice();

                nodeItem.put("name", cart.getLineItems().get(i).getProduct().getName());
                nodeItem.put("quantity", quantity);
                nodeItem.put("price", customerClientUtil.formatAmount(price));
                //get over the extended price
                long extendedPrice = price * quantity;

                nodeItem.put("extendedPrice", customerClientUtil.formatAmount(extendedPrice));
                //get subtotal
                subtotal += extendedPrice;

                itemsArrayNode.add(nodeItem);
            }

            node.put("subtotal", customerClientUtil.formatAmount(subtotal));

            //get discount
            long discountAmount;
            AccountDiscount discountType = (AccountDiscount) session.getAttribute("ACCOUNT_DISCOUNT");
            if (discountType != null) {
                float discountPercent = Float.parseFloat(discountRepo.findDiscountPercent(discountType.getCode().getCode()));
                float discountDecimal = discountPercent / 100;

                discountAmount = (long) (subtotal * discountDecimal);
            } else {
                discountAmount = 0;
            }
            node.put("discount", customerClientUtil.formatAmount(discountAmount));

            //discounted subtotal
            long discountedSubtotal = subtotal - discountAmount;
            node.put("discounted_subtotal", customerClientUtil.formatAmount(discountedSubtotal));

            //calculate Taxes
            long taxes = (long) (discountedSubtotal * TAX_RATE);
            node.put("taxes", customerClientUtil.formatAmount(taxes));

            //is order pickup or shipped
            if(session.getAttribute("PICKUP").equals("true")){
                shippingRate = 0;
                node.put("shipping", shippingRate);
            }
            else {
                //flat shipping rate
                node.put("shipping", customerClientUtil.formatAmount(shippingRate));
            }

            //calculate total
            long total = discountedSubtotal + taxes + shippingRate;
            node.put("Total", customerClientUtil.formatAmount(total));

            mainArrayNode.add(itemsArrayNode);
            mainArrayNode.add(node);
        }

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
}