package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.DiscountRepo;
import ca.sait.vezorla.repository.ProductRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User service
 *
 * @author matthewjflee, jjrr1717
 */
@Service
public class UserServicesImp implements UserServices {

    private ProductRepo productRepo;
    private AccountRepo accountRepo;
    private DiscountRepo discountRepo;
    private ObjectMapper mapper;
    private CustomerClientUtil customerClientUtil;

    public UserServicesImp(ProductRepo productRepo, AccountRepo accountRepo, DiscountRepo discountRepo, ObjectMapper mapper) {
        this.productRepo = productRepo;
        this.accountRepo = accountRepo;
        this.discountRepo = discountRepo;
        this.mapper = mapper;
        this.customerClientUtil = new CustomerClientUtil();
    }

    public void applyDiscount(Discount discount) {

    }

    /**
     * Get the customer's cart from the session
     *
     * @param session
     * @return
     * @author matthewjflee, jjrr1717
     */
    public Cart getSessionCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
        }

        return cart;
    }

    /**
     * Adding line item to cart
     *
     * @param lineItem
     * @param request
     * @return
     * @author matthewjflee, jjrr1717
     */
    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute("CART");

        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("CART", cart);
        }

        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();
        lineItems.add(lineItem);
        cart.setLineItems(lineItems);
        request.getSession().setAttribute("CART", cart);

        return cart;
    }

    /**
     * Get the total quantity of all line items in the cart
     *
     * @param lineItems
     * @return
     * @author matthewjflee, jjrr1717
     */
    public String getTotalCartQuantity(ArrayList<LineItem> lineItems) {
        //loop through lineItems to get total quantity on order
        int counter = lineItems.stream().mapToInt(LineItem::getQuantity).sum();
        return counter + "";
    }

    /**
     * Get the total quantity of the product from the Products database
     *
     * @param id
     * @return
     * @author matthewjflee, jjrr1717
     */
    public int getProductQuantity(Long id) {
        int quantity = productRepo.findTotalQuantity(id);
        return quantity;
    }

    /**
     * Validate the order quantity before adding the product to the cart as a line item
     *
     * @param orderedQuantitySent
     * @param inStockQuantity
     * @return
     * @author matthewjflee, jjrr1717
     */
    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity) {
        orderedQuantitySent = orderedQuantitySent.replaceAll("\"", "");
        int orderedQuantity = Integer.parseInt(orderedQuantitySent);
        return inStockQuantity - orderedQuantity;
    }

    public ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpServletRequest request) {
        ArrayNode outOfStockItems = mapper.createArrayNode();
        //check quantity in stock for each item
        for (int i = 0; i < cart.getLineItems().size(); i++) {
            ObjectNode outOfStockNode = mapper.createObjectNode();
            int quantityInStock = getProductQuantity(cart.getLineItems().get(i).getProduct().getProdId());
            int checkQuantity = validateOrderedQuantity(cart.getLineItems().get(i).getQuantity() + "", quantityInStock);

            if (checkQuantity < 0) {
                checkQuantity = Math.abs(checkQuantity);

                //update cart to only include amount that can be ordered
                updateLineItemSession(cart.getLineItems().get(i).getProduct().getProdId(),
                        quantityInStock, cart, request);

                outOfStockNode.put("name", cart.getLineItems().get(i).getProduct().getName());
                outOfStockNode.put("by", checkQuantity);
                outOfStockItems.add(outOfStockNode);
            }

        }

        System.out.println("Check" + outOfStockItems);

        return outOfStockItems;
    }

    @Override
    public void createLineItems(Product product) {

    }

    /**
     * Create a line item from the product for a customer
     *
     * @param product
     * @param sentQuantity
     * @param request
     * @return
     * @author matthewjflee, jjrr1717
     */
    public LineItem createLineItemSession(Optional<Product> product, String sentQuantity, HttpServletRequest request) {
        LineItem lineItem = new LineItem();
        sentQuantity = sentQuantity.replaceAll("\"", ""); //Sending a string so replace \
        int quantity = Integer.parseInt(sentQuantity);

        lineItem.setQuantity(quantity);
        lineItem.setCurrentName(product.get().getName());
        lineItem.setCurrentPrice(product.get().getPrice());
        lineItem.setCart((Cart) request.getSession().getAttribute("CART"));
        lineItem.setProduct(product.get());

        return lineItem;
    }

    /**
     * Update line item quantity in cart
     *
     * @param id
     * @param quantity
     * @param cart
     * @author matthewjflee, jjrr1717
     */
    public boolean updateLineItemSession(Long id, int quantity, Cart cart, HttpServletRequest request) {
        boolean result = false;
        ArrayList<LineItem> lineItems = (ArrayList) cart.getLineItems();
        for (int i = 0; i < lineItems.size(); i++) {
            if (lineItems.get(i).getProduct().getProdId().equals(id)) {
                lineItems.get(i).setQuantity(quantity);
                result = true;
            }
        }
        request.getSession().setAttribute("CART", cart);

        return result;

    }

    /**
     * Remove a line item from the customer's cart
     *
     * @param id
     * @param cart
     * @param request
     * @return
     * @author matthewjflee, jjrr1717
     */
    public boolean removeLineItemSession(Long id, Cart cart, HttpServletRequest request) {
        boolean result = false;
        ArrayList<LineItem> lineItems = (ArrayList) cart.getLineItems();
        for (int i = 0; i < lineItems.size(); i++) {
            if (lineItems.get(i).getProduct().getProdId().equals(id)) {
                lineItems.remove(lineItems.get(i));
                result = true;
            }
        }

        request.getSession().setAttribute("CART", cart);

        return result;
    }

    public void createLineItems(Long id) {

    }

    /**
     * Create an account in the Accounts table
     *
     * @param account
     * @return
     * @author matthewjflee, jjrr1717
     */
    public boolean saveAccount(Account account) {
        boolean result = false;
        Account saved = accountRepo.save(account);
        if (saved != null)
            result = true;

        return result;
    }

    /**
     * Return all products in the Products table
     *
     * @return
     * @author kwistech
     */
    public List<Product> getAllProducts() {

        return new ArrayList<>(productRepo.findAll());
    }

    public Cart getCart() {
        return null;
    }

    public List<Lot> getLots(Long id) {
        return null;
    }

    /**
     * Return a specified product
     *
     * @param id
     * @return
     * @author matthewjflee, jjrr1717
     */
    public Optional<Product> getProduct(Long id) { //It wanted Optionals

        return productRepo.findById(id);
    }

    public void getStoreProducts(Long id) {

    }

    /**
     * Method to return valid discounts a user
     * can apply to their order
     *
     * @param
     * @return
     * @author matthewjflee, jjrr1717
     */
    public ArrayList<Discount> getValidDiscounts(String email) {
        //get current date for comparison
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        //get all discounts within that time range
        ArrayList<String> stringDiscounts = (ArrayList<String>) discountRepo.findValidDiscounts(sqlDate, email);
        //list of Discounts
        ArrayList<Discount> discounts = new ArrayList<>();
        System.out.println("valid discounts " + stringDiscounts.size());

        //parse the comma deliminted string returned from query
        for (String s : stringDiscounts) {
            System.out.println("discount FATT GARRETTT   " + s);

            String[] spl = s.split(",");
            float decimalPercent = Float.parseFloat(spl[2]) / 100;
            Discount discount = new Discount(spl[0], spl[1], decimalPercent);
            discounts.add(discount);
        }
        return discounts;
//        return null;
    }

    /**
     * Method to create the discount code and percent json to
     * send to front end.
     *
     * @param session
     * @param arrayNode
     * @return
     */
    public ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode) {
        Account currentAccount = (Account) session.getAttribute("ACCOUNT");
        ArrayList<Discount> discounts = (ArrayList<Discount>) getValidDiscounts(currentAccount.getEmail());
        for (int i = 0; i < discounts.size(); i++) {
            ObjectNode node = mapper.createObjectNode();
            node.put("code", discounts.get(i).getCode());
            node.put("description", discounts.get(i).getDescription());
            node.put("percent", discounts.get(i).getPercent());
            arrayNode.add(node);
        }

        return arrayNode;
    }

    /**
     * Method to get the selected discount from the user.
     * Creates an AccountDiscount with the account in
     * the session and the discount code provided by the
     * front-end body.
     *
     * @param code
     * @param request
     * @param session
     */
    public void getSelectedDiscount(String code, HttpServletRequest request, HttpSession session) {
        session = request.getSession();

        //get user email from session
        Account account = (Account) session.getAttribute("ACCOUNT");
        String discountCode = code.replaceAll("\"", "");
        //create discount object
        Discount discount = new Discount();
        discount.setCode(discountCode);
        //create a AccountDiscount object
        AccountDiscount holdDiscount = new AccountDiscount(account, discount);

        //store account_discount into session
        session.setAttribute("ACCOUNT_DISCOUNT", holdDiscount);

    }

    public ArrayNode viewSessionCart(HttpServletRequest request, Cart cart) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ArrayNode arrayNode = mapper.createArrayNode();
//        ArrayNode outOfStockItems = checkItemsOrderedOutOfStock(cart, request);
        for (int i = 0; i < cart.getLineItems().size(); i++) {
            ObjectNode node = mapper.createObjectNode();
            node.put("prodID", cart.getLineItems().get(i).getProduct().getProdId());
            node.put("name", cart.getLineItems().get(i).getProduct().getName());
            node.put("price", customerClientUtil.formatAmount(cart.getLineItems().get(i).getProduct().getPrice()));
            node.put("imageMain", cart.getLineItems().get(i).getProduct().getImageMain());
            node.put("quantity", cart.getLineItems().get(i).getQuantity());
            arrayNode.add(node);
        }

//        if(outOfStockItems != null) {
//            System.out.println("out " + outOfStockItems);
//            arrayNode.add(outOfStockItems);
//        }


        return arrayNode;
    }

    /**
     * Method to parse the json sent from
     * the front end for the shipping information
     *
     * @param httpEntity
     * @param request
     * @param json
     * @return
     * @throws InvalidInputException
     * @throws JsonProcessingException
     */
    public String getShippingInfo(HttpEntity<String> httpEntity, HttpServletRequest request, String json) throws InvalidInputException, JsonProcessingException {
        HttpSession session = request.getSession();
        boolean created = false;
        try {
            Object obj = new JSONParser().parse(json);
            JSONObject jo = (JSONObject) obj;
            String email = (String) jo.get("email");
            String firstName = (String) jo.get("firstName");
            String lastName = (String) jo.get("lastName");
            String phoneNumber = (String) jo.get("phoneNumber");
            String pickup = (String) jo.get("pickup");
            try {
                customerClientUtil.validatePhoneNumber(phoneNumber);
            } catch (InvalidInputException e) {

            }
            String address = (String) jo.get("address");
            String city = (String) jo.get("city");
            String province = (String) jo.get("province");
            String country = (String) jo.get("country");
            String postalCode = (String) jo.get("postalCode");
            try {
                customerClientUtil.validatePostalCode(postalCode);
            } catch (InvalidInputException e) {

            }

            if (email == null || firstName == null || lastName == null ||
                    postalCode == null || phoneNumber == null) {
                throw new InvalidInputException();
            }

            Account newAccount = new Account(email, lastName, firstName,
                    phoneNumber, address, city, country, province, postalCode);

            session.setAttribute("ACCOUNT", newAccount);
            session.setAttribute("PICKUP", pickup);
            created = true;
        } catch (ParseException e) {
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(created);
    }

    public ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart) {

        ObjectNode root = mapper.createObjectNode();
        ObjectNode node = mapper.createObjectNode();
        ArrayNode itemsArrayNode = mapper.createArrayNode();


        long subtotal = 0;
        long shippingRate = 1000;
        final float TAX_RATE = 0.05f;

        for (int i = 0; i < cart.getLineItems().size(); i++) {
            ObjectNode nodeItem = mapper.createObjectNode();
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
        if (session.getAttribute("PICKUP").equals("true")) {
            shippingRate = 0;
            node.put("shipping", shippingRate);
        } else {
            //flat shipping rate
            node.put("shipping", customerClientUtil.formatAmount(shippingRate));
        }

        //calculate total
        long total = discountedSubtotal + taxes + shippingRate;
        node.put("Total", customerClientUtil.formatAmount(total));

        mainArrayNode.add(itemsArrayNode);
        mainArrayNode.add(node);

        //create temporary invoice
        Invoice invoice = new Invoice(discountedSubtotal, discountAmount, taxes, total);
        session.setAttribute("INVOICE", invoice);

        return mainArrayNode;
    }

    public List<Lot> obtainSufficientQtyLots() {
        return null;
    }

    public boolean searchEmail(String email) {
        return false;
    }

    public boolean subscribeEmail(String email) {
        return false;
    }

}
