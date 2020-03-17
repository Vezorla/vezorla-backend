package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.DiscountRepo;
import ca.sait.vezorla.repository.ProductRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public UserServicesImp(ProductRepo productRepo, AccountRepo accountRepo, DiscountRepo discountRepo, ObjectMapper mapper) {
        this.productRepo = productRepo;
        this.accountRepo = accountRepo;
        this.discountRepo = discountRepo;
        this.mapper = mapper;
    }

    public void applyDiscount(Discount discount) {

    }

    /**
     * Get the customer's cart from the session
     *
     * @author matthewjflee, jjrr1717
     * @param session
     * @return
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
     * @author matthewjflee, jjrr1717
     * @param lineItem
     * @param request
     * @return
     */
    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute("CART");

        if (cart == null) {
            System.out.println("cart is null");
            cart = new Cart();
            request.getSession().setAttribute("CART", cart);
        }

        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();
        System.out.println("list size old: " + lineItems.size());
        lineItems.add(lineItem);
        System.out.println("new size " + lineItems.size());
        cart.setLineItems(lineItems);
        request.getSession().setAttribute("CART", cart);

        return cart;
    }

    /**
     * Get the total quantity of all line items in the cart
     *
     * @author matthewjflee, jjrr1717
     * @param lineItems
     * @return
     */
    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems) {
        //loop through lineItems to get total quantity on order
        int counter = lineItems.stream().mapToInt(LineItem::getQuantity).sum();
        return counter + "";
    }

    /**
     * Get the total quantity of the product from the Products database
     *
     * @author matthewjflee, jjrr1717
     * @param id
     * @return
     */
    public int getProductQuantity(Long id) {
        int quantity = productRepo.findTotalQuantity(id);

        return quantity;
    }

    /**
     * Validate the order quantity before adding the product to the cart as a line item
     *
     * @author matthewjflee, jjrr1717
     * @param orderedQuantitySent
     * @param inStockQuantity
     * @return
     */
    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity) {
        orderedQuantitySent = orderedQuantitySent.replaceAll("\"", "");
        int orderedQuantity = Integer.parseInt(orderedQuantitySent);
        return inStockQuantity - orderedQuantity;
    }

    @Override
    public void createLineItems(Product product) {

    }

    /**
     * Create a line item from the product for a customer
     *
     * @author matthewjflee, jjrr1717
     * @param product
     * @param sentQuantity
     * @param request
     * @return
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
     * @author matthewjflee, jjrr1717
     * @param id
     * @param quantity
     * @param cart
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
     * @author matthewjflee, jjrr1717
     * @param id
     * @param cart
     * @param request
     * @return
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
     * Create and persist an account in the Accounts table
     *
     * @author matthewjflee, jjrr1717
     * @param account
     * @return
     */
    public boolean createAccount(Account account) {
        boolean result = false;
        Account saved = accountRepo.save(account);
        if (saved != null)
            result = true;

        return result;
    }

    public Optional<Account> findAccount(String email) {
        return accountRepo.findById(email);
    }

    /**
     * Return all products in the Products table
     *
     * @author kwistech
     * @return
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
     * @author matthewjflee, jjrr1717
     * @param id
     * @return
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
     * @author matthewjflee, jjrr1717
     * @param
     * @return
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
        for(String s : stringDiscounts) {
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
     * Method to get the selected discount from the user.
     * Creates an AccountDiscount with the account in
     * the session and the discount code provided by the
     * front-end body.
     * @param code
     * @param request
     * @param session
     */
    public void getSelectedDiscount(String code, HttpServletRequest request, HttpSession session){
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

        System.out.println(holdDiscount.getCode().getCode());
        System.out.println(holdDiscount.getEmail().getEmail());

    }

    public ArrayNode viewSessionCart(HttpSession session) throws JsonProcessingException {
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

        return arrayNode;
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
