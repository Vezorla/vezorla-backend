package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class UserServicesImp implements UserServices {

    private ProductRepo productRepo;
    private AccountRepo accountRepo;
    private DiscountRepo discountRepo;
    private LotRepo lotRepo;
    private InvoiceRepo invoiceRepo;
    private LineItemRepo lineItemRepo;
    private CartRepo cartRepo;
    private AccountDiscountRepo accountDiscountRepo;
    private ObjectMapper mapper;

    /**
     * Method to save the discount used on
     * a order to the Account_Discount table
     * @author jjrr1717
     * @param request for the session
     */
    public void applyDiscount(HttpServletRequest request) {

        //get discount from the session
        HttpSession session = request.getSession();
        AccountDiscount accountDiscount = (AccountDiscount) session.getAttribute("ACCOUNT_DISCOUNT");

        if (!accountDiscount.getCode().getCode().equals("NotSelected")) {
            accountDiscountRepo.insertWithQuery(accountDiscount);
        }

    }

    /**
     * Get the customer's cart from the session
     *
     * @param session to access the cart
     * @return the cart
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
     * @param lineItem to add to the cart
     * @param request for the session
     * @return the cart with new line item added
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
     * @param lineItems to count the total quantity in them
     * @return the quantity as a String
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
     * @param id the product's id to check.
     * @return the quantity
     * @author matthewjflee, jjrr1717
     */
    public int getProductQuantity(Long id) {
        int quantity = productRepo.findTotalQuantity(id);
        return quantity;
    }

    /**
     * Validate the order quantity before adding the product to the cart as a line item
     *
     * @param orderedQuantitySent the quantity wanted to add to the line item
     * @param inStockQuantity quantity currently in stock in database
     * @return the difference. >=0 means there is enough stock. >0 means
     *          there is not enough stock.
     * @author matthewjflee, jjrr1717
     */
    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity) {
        orderedQuantitySent = orderedQuantitySent.replaceAll("\"", "");
        int orderedQuantity = Integer.parseInt(orderedQuantitySent);
        return inStockQuantity - orderedQuantity;
    }

    /**
     * Method to return the items out of stock that are on
     * an order.
     * Would have become out of stock sometime between the
     * checkout process
     * @author jjrr1717
     * @param cart to check if any items are out of stock
     * @param request for the session
     * @return an ArrayNode of all the out of stock items for front end
     *          to use.
     */
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

        return outOfStockItems;
    }

    /**
     * Create a line item from the product for a customer
     *
     * @param product for the line item
     * @param sentQuantity quantity for the line item
     * @param request for the session
     * @return line item to be added to the session
     * @author matthewjflee, jjrr1717
     */
    public LineItem createLineItemSession(Optional<Product> product, String sentQuantity, HttpServletRequest request) {
        //check if product already exists
        boolean exists = checkProductLineItem(product.get().getProdId(), request);

        LineItem lineItem = null;
        if (!exists) {
            lineItem = new LineItem();
            sentQuantity = sentQuantity.replaceAll("\"", ""); //Sending a string so replace \
            int quantity = Integer.parseInt(sentQuantity);

            lineItem.setQuantity(quantity);
            lineItem.setCurrentName(product.get().getName());
            lineItem.setCurrentPrice(product.get().getPrice());
            lineItem.setCart((Cart) request.getSession().getAttribute("CART"));
            lineItem.setProduct(product.get());
        } else {
            updateLineItemAdd(product.get(), sentQuantity, request);
        }

        return lineItem;
    }

    /**
     * Method to search for the existence of the
     * line item.
     *
     * @param id of the product
     * @param request for the session
     * @return boolean true if it exits in cart already.
     * @author jjrr1717
     */
    private boolean checkProductLineItem(Long id, HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession();
        Cart cart = getSessionCart(session);

        for (int i = 0; i < cart.getLineItems().size() && result == false; i++) {
            if (cart.getLineItems().get(i).getProduct().getProdId().equals(id)) {
                result = true;
            }
        }
        return result;
    }


    /**
     * Method to update a line item that already exits in the cart.
     * It will add the quantity to the existing quantity.
     * @param product the product already in the cart
     * @param sentQuantity the quantity to add to the line item
     * @param request for the session
     * @author jjrr1717
     */
    private void updateLineItemAdd(Product product, String sentQuantity, HttpServletRequest request) {
        HttpSession session = request.getSession();

        //get cart with line items
        Cart cart = (Cart) session.getAttribute("CART");
        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();
        sentQuantity = sentQuantity.replaceAll("\"", "");
        int quantity = Integer.parseInt(sentQuantity);

        //loop through to find line item to add additional quantity
        for(int i = 0; i < lineItems.size(); i++) {
            if(lineItems.get(i).getProduct().getProdId().equals(product.getProdId())) {
                lineItems.get(i).setQuantity(lineItems.get(i).getQuantity() + quantity);
            }
        }

        cart.setLineItems(lineItems);
        session.setAttribute("CART", cart);
    }

    /**
     * Update line item quantity in cart.
     * At the review page.
     *
     * @param id of the product to update
     * @param quantity of the line item
     * @param cart to add updated line item
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
     * @param id of product to remove
     * @param cart to remove product from
     * @param request the session
     * @return a boolean true if successfully removed, otherwise false.
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

    /**
     * Create and persist an account in the Accounts table
     *
     * @param account to persist in database
     * @return boolean true if it was succesfully added, otherwise false
     * @author matthewjflee, jjrr1717
     */
    public boolean saveAccount(Account account) {
        boolean result = false;
        Account saved = accountRepo.save(account);
        if (saved != null)
            result = true;

        return result;
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepo.findById(email);
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
     * @param email a String for the account
     *              that is requesting discounts.
     * @return an ArrayList of valid discounts that can be
     *              applied to the order.
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

        //parse the comma delimited string returned from query
        for (String s : stringDiscounts) {

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
     * @param session for the current session
     * @param arrayNode to add the json
     * @return ArrayNode containing the discount code and percent
     * @author jjrr1717
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
     * @param code for the discount
     * @param request the session
     * @param session the current session
     * @author jjrr1717
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

    /**
     * Method to send information about the cart to the front-end
     * @param request the session
     * @param cart to view
     * @return ArrayNode containing the information for the cart to view
     * @throws JsonProcessingException
     * @author jjrr1717, matthewjflee
     */
    public ArrayNode viewSessionCart(HttpServletRequest request, Cart cart) throws JsonProcessingException {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();
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
        return arrayNode;
    }

    /**
     * Method to parse the json sent from
     * the front end for the shipping information
     *
     * @param request the session
     * @return String boolean if account is created successfully for shipping info
     * @throws InvalidInputException
     * @throws JsonProcessingException
     */
    public String getShippingInfo(HttpServletRequest request, Account account) throws InvalidInputException, JsonProcessingException {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();
        HttpSession session = request.getSession();
        boolean created = false;
        if (account.getEmail() == null || account.getFirstName() == null || account.getLastName() == null ||
                account.getPostalCode() == null || account.getPhoneNum() == null) {
            throw new InvalidInputException();
        }

        customerClientUtil.validatePhoneNumber(account.getPhoneNum());
        customerClientUtil.validatePostalCode(account.getPostalCode());

        session.setAttribute("ACCOUNT", account);
        session.setAttribute("PICKUP", account.getPickup());
        created = saveAccount(account);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(created);
    }

    /**
     * Method to create ArrayNode of all the information that
     * will be displayed on the review order page.
     *
     * @param session of the current session
     * @param mainArrayNode the node containing all the information
     * @param cart that contains all the information
     * @return ArrayNode with all the information needed to review the
     *          order.
     * @author jjrr1717 (unfortunately - sorry for the bad coding)
     */
    public ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart) {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();

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
        if (discountType.getCode().getCode().equals("NotSelected")) {
            discountAmount = 0;
        } else if (discountType != null) {
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
        System.out.println("Shipping in review: " + shippingRate);
        //create temporary invoice
        Invoice invoice = new Invoice(shippingRate,
                discountedSubtotal,
                discountAmount,
                taxes,
                total);
        session.setAttribute("INVOICE", invoice);

        return mainArrayNode;
    }

    /**
     * Method to decrease inventory from lot
     * in database.
     * @param request the session
     * @author jjrr1717
     */
    public void decreaseInventory(HttpServletRequest request) {
        //get line items to determine what was sold
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("CART");
        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();

        //loop through the items in the order
        for (int i = 0; i < lineItems.size(); i++) {
            ArrayList<Lot> lotsToUse = (ArrayList<Lot>) obtainSufficientQtyLots(lineItems.get(i).getQuantity(), lineItems.get(i).getProduct());

            int orderedQty = lineItems.get(i).getQuantity();
            for (int j = 0; j < lotsToUse.size() && orderedQty > 0; j++) {
                int lotQuantity = lotsToUse.get(j).getQuantity();

                if (lotQuantity >= orderedQty) {
                    lotsToUse.get(j).setQuantity(lotQuantity - orderedQty);
                    lotRepo.save(lotsToUse.get(j));
                    orderedQty = 0;
                } else if (lotQuantity < orderedQty) {
                    lotsToUse.get(j).setQuantity(0);
                    lotRepo.save(lotsToUse.get(j));
                    orderedQty -= lotQuantity;
                }

            }
        }
    }


    /**
     * Method to obtain lots that contain
     * enough quantity to fulfill order.
     *
     * @param qty needed to fulfill order
     * @param product for the order
     * @return List of the lots that can fulfill the order
     * @author jjrr1717
     */
    public List<Lot> obtainSufficientQtyLots(int qty, Product product) {
        //grab lots from database
        ArrayList<Lot> lots = (ArrayList<Lot>) lotRepo.findAllLotsWithQuantity(product);
        ArrayList<Lot> lotsToUse = new ArrayList<>();

        //get the lots to be used for the order
        for (int i = 0; i < lots.size() && qty > 0; i++) {
            int qtyInLot = lots.get(i).getQuantity();
            int result = qtyInLot - qty;

            if (result >= 0) {
                lotsToUse.add(lots.get(i));
                qty = 0;
            } else {
                lotsToUse.add(lots.get(i));
                qty -= lots.get(i).getQuantity();
            }
        }

        return lotsToUse;
    }

    /**
     * Method to get all the invoice information
     * and save it to the database.
     *
     * @param request for the session
     * @return the Invoice saved to the database
     * @author jjrr1717
     */
    public Invoice saveInvoice(HttpServletRequest request) {
        //grab the invoice already generated
        HttpSession session = request.getSession();
        Invoice newInvoice = (Invoice) session.getAttribute("INVOICE");

        System.out.println("Shipping cost at Save Invoice: " + newInvoice.getShippingCost());

        //get current date
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        //grab the account created
        Account account = (Account) session.getAttribute("ACCOUNT");

        //get pickup status
        String pickup = (String) session.getAttribute("PICKUP");

        //add the above to the newInvoice
        newInvoice.setDate(sqlDate);
        newInvoice.setAccount(account);
        newInvoice.setPickup(pickup);

        //save to database
        return invoiceRepo.save(newInvoice);

    }

    /**
     * Method to save cart and line items to database
     *
     * @param request for the session
     * @param invoice the line items belong to
     * @author jjrr1717
     */
    public void saveLineItems(HttpServletRequest request, Invoice invoice) {
        HttpSession session = request.getSession();

        //grab the cart to get the line items
        Cart cart = (Cart) session.getAttribute("CART");
        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();

        //persist cart because it is a parent
        cartRepo.save(cart);

        //loop through lineitems. Assign cart number & invoice number and persist line item
        for (int i = 0; i < lineItems.size(); i++) {
            lineItems.get(i).setInvoice(invoice);
            lineItems.get(i).setCart(cart);
            lineItemRepo.save(lineItems.get(i));
        }
    }

    /**
     * Apply line items to the invoice.
     * Line items are found in the database
     * @param invoice the invoice to apply
     *         the line items to.
     * @author jjrr1717
     */
    public void applyLineItemsToInvoice(Invoice invoice) {
        //obtain the line items from database
        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) lineItemRepo.findLineItemByInvoice(invoice);

        //add these to the invoice
        invoice.setLineItemList(lineItems);
    }
}

