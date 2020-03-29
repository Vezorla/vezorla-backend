package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.OutOfStockException;
import ca.sait.vezorla.exception.UnauthorizedException;
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
    private DiscountRepo discountRepo;
    private LotRepo lotRepo;
    private InvoiceRepo invoiceRepo;
    private LineItemRepo lineItemRepo;
    private CartRepo cartRepo;
    private AccountDiscountRepo accountDiscountRepo;
    private AccountServices accountServices;
    private EmailServices emailServices;
    private ObjectMapper mapper;

    /**
     * Return all products in the Products table
     *
     * @return array node of all products
     * @author jjrr1717
     */
    public ArrayNode getAllProducts(ObjectMapper mapper) {
        CustomerClientUtil ccu = new CustomerClientUtil();

        //obtain all the products
        List<Product> products = productRepo.findAll();

        //create custom json
        ArrayNode productsNode = mapper.createArrayNode();

        //loop through products
        for (Product product : products) {
            ObjectNode node = productsNode.objectNode();

            getProductInfo(ccu, productsNode, product, node);
        }

        return productsNode;
    }

    /**
     * Return a specified product
     *
     * @param id product ID
     * @return product
     * @author matthewjflee, jjrr1717
     */
    public ArrayNode getProduct(Long id, ObjectMapper mapper) {
        CustomerClientUtil ccu = new CustomerClientUtil();
        ArrayNode productsNode = mapper.createArrayNode();
        Product product;

        //obtain all the products
        Optional<Product> findProduct = productRepo.findById(id);
        if (findProduct.isPresent()) {
            product = findProduct.get();

            ObjectNode node = productsNode.objectNode();
            getProductInfo(ccu, productsNode, product, node);
        }

        return productsNode;
    }

    /**
     * Return an array node of product info
     *
     * @param ccu          format price
     * @param productsNode list to add to
     * @param product      Product
     * @param node         node for product info
     * @author matthewjflee, jjrr1717
     */
    private void getProductInfo(CustomerClientUtil ccu, ArrayNode productsNode, Product product, ObjectNode node) {
        node.put("prodId", product.getProdId());
        node.put("name", product.getName());
        node.put("description", product.getDescription());
        node.put("subdescription", product.getSubdescription());
        node.put("harvestTime:", product.getHarvestTime());
        node.put("imageMain", product.getImageMain());
        node.put("imageOne", product.getImageOne());
        node.put("imageTwo", product.getImageTwo());
        node.put("imageThree", product.getImageThree());
        node.put("active", product.isActive());
        node.put("threshold", product.getThreshhold());
        node.put("price", ccu.formatAmount(product.getPrice()));
        node.put("oldPrice", ccu.formatAmount(product.getOldPrice()));

        productsNode.add(node);
    }


    /**
     * Return a specified product
     *
     * @param id Product ID
     * @return product
     * @author matthewjflee, jjrr1717
     */
    public Optional<Product> getProduct(Long id) {
        return productRepo.findById(id);
    }

    /**
     * Get the customer's cart from the session
     *
     * @param session to access the cart
     * @return the cart
     * @author matthewjflee, jjrr1717
     */
    public Cart getCart(HttpSession session) {
        Cart cart;
        Account account = (Account) session.getAttribute("ACCOUNT");

        //Customer
        if (account == null || !account.isUserCreated()) {
            cart = (Cart) session.getAttribute("CART");
            if (cart == null)
                cart = new Cart();
        } else { //Client
            cart = accountServices.findRecentCart(account);
        }

        return cart;
    }

    /**
     * Adding line item to cart
     *
     * @param lineItems list of line items to set in the cart
     * @param cart      for the session
     * @param session   user's session
     * @author matthewjflee, jjrr1717
     */
    public void addLineItemToSessionCart(List<LineItem> lineItems, Cart cart, HttpSession session) {
        cart.setLineItems(lineItems);
        session.setAttribute("CART", cart);
    }

    /**
     * Get the total quantity of all line items in the cart
     *
     * @param lineItems to count the total quantity in them
     * @return the quantity as a String
     * @author matthewjflee, jjrr1717
     */
    public String getTotalCartQuantity(List<LineItem> lineItems) {
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
     * @author matthewjflee, jjrr1717
     */
    public int getProductQuantity(Long id) {
        return productRepo.findTotalQuantity(id);
    }

    /**
     * Validate the order quantity before adding the product to the cart as a line item
     *
     * @param orderedQuantity the quantity wanted to add to the line item
     * @param inStockQuantity quantity currently in stock in database
     * @return the difference. >=0 means there is enough stock. >0 means
     * there is not enough stock.
     * @author matthewjflee, jjrr1717
     */
    public int validateOrderedQuantity(int orderedQuantity, int inStockQuantity) {
        return inStockQuantity - orderedQuantity;
    }

    /**
     * Method to return the items out of stock that are on
     * an order.
     * Would have become out of stock sometime between the
     * checkout process
     *
     * @param cart    to check if any items are out of stock
     * @param session for the session
     * @return an ArrayNode of all the out of stock items for front end
     * to use.
     * @author jjrr1717
     */
    public ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpSession session) throws OutOfStockException {
        ArrayNode outOfStockItems = mapper.createArrayNode();

        //check quantity in stock for each item
        for (int i = 0; i < cart.getLineItems().size(); i++) {
            LineItem li = cart.getLineItems().get(i);

            int quantityInStock = getProductQuantity(li.getProduct().getProdId());
            int checkQuantity = validateOrderedQuantity(li.getQuantity(), quantityInStock);

            if (checkQuantity < 0) {
                checkQuantity = Math.abs(checkQuantity);

                //update cart to only include amount that can be ordered
                updateLineItem(li.getProduct().getProdId(),
                        quantityInStock, cart, session);

                ObjectNode outOfStockNode = mapper.createObjectNode();
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
     * @param product  for the line item
     * @param quantity quantity for the line item
     * @param cart     user's cart
     * @return line item to be added to the session
     * @author matthewjflee, jjrr1717
     */
    public List<LineItem> createLineItem(Product product, int quantity, Cart cart) {
        //Get cart
        LineItem lineItem;
        List<LineItem> lineItems;

        //check if product already exists
        int lineItemIndex = checkProductLineItem(product.getProdId(), cart);
        if (lineItemIndex == -1) {
            //Create line item
            lineItem = new LineItem(quantity, product.getName(), product.getPrice(), cart, product);
            lineItems = cart.getLineItems();
            lineItems.add(lineItem);
        } else {
            lineItems = updateLineItemQuantity(quantity, cart, lineItemIndex);
        }

        return lineItems;
    }

    /**
     * Method to search for the existence of the
     * line item in the cart.
     * <p>
     * Will return the index of the line item in the cart
     * If the line item does not exist, -1 is returned
     *
     * @param id   of the product
     * @param cart user's cart
     * @return index of line item in cart
     * @author jjrr1717, matthewjflee
     */
    private int checkProductLineItem(Long id, Cart cart) {
        List<LineItem> lineItems = cart.getLineItems();

        for (int i = 0; i < lineItems.size(); i++) {
            LineItem li = lineItems.get(i);

            if (li.getProduct().getProdId().equals(id)) {
                return i;
            }
        }

        return -1;
    }


    /**
     * Method to update a line item that already exits in the cart.
     * It will add the quantity to the existing quantity.
     *
     * @param quantity the quantity to add to the line item
     * @param cart     user's cart
     * @param index    of line item
     * @return list of line items
     * @author jjrr1717, matthewjflee
     */
    private List<LineItem> updateLineItemQuantity(int quantity, Cart cart, int index) {
        List<LineItem> lineItems = cart.getLineItems();

        //Set quantity
        int currentQuantity = lineItems.get(index).getQuantity();
        lineItems.get(index).setQuantity(currentQuantity + quantity);

        return lineItems;
    }

    /**
     * Update line item quantity in cart.
     * At the review page.
     *
     * @param id       of the product to update
     * @param quantity of the line item
     * @param cart     to add updated line item
     * @param session  user session
     * @author matthewjflee, jjrr1717
     */
    public boolean updateLineItem(long id, int quantity, Cart cart, HttpSession session) throws OutOfStockException {
        boolean inStock = checkIfLineItemInStock(id, quantity);

        if(!inStock){
            throw new OutOfStockException();
        }

        boolean result = false;
        List<LineItem> lineItems = cart.getLineItems();
        LineItem lineItem = null;

        for (int i = 0; i < lineItems.size() && !result; i++) {
            if (lineItems.get(i).getProduct().getProdId() == id) {
                lineItem = lineItems.get(i);
                lineItem.setQuantity(quantity);
                result = true;
            }
        }

        if (cart.isFromAccount()) {
            if (lineItem != null)
                accountServices.saveLineItem(lineItem);
        } else
            session.setAttribute("CART", cart);

        return result;
    }

    /**
     * Method to check if a single line item is out
     * of stock during update.
     *
     * @param id of product
     * @param updatedQty is new qty
     * @return boolean true if item is in stock, otherwise false
     */
    public boolean checkIfLineItemInStock(long id, int updatedQty){
        boolean inStock = true;

        int inStockQty = getProductQuantity(id);
        if(updatedQty > inStockQty){
            inStock = false;
        }

        return inStock;

    }


    /**
     * Remove a line item from the customer's cart
     *
     * @param id      of product to remove
     * @param cart    to remove product from
     * @param session the session
     * @return a boolean true if successfully removed, otherwise false.
     * @author matthewjflee, jjrr1717
     */
    public boolean removeLineItem(long id, Cart cart, HttpSession session) {
        boolean result = false;
        long deleteLineNum = -1;
        List<LineItem> lineItems = cart.getLineItems();

        for (int i = 0; i < lineItems.size() && !result; i++) {
            if (lineItems.get(i).getProduct().getProdId() == id) {
                if (!cart.isFromAccount())
                    lineItems.remove(lineItems.get(i));
                else
                    deleteLineNum = lineItems.get(i).getLineNum();
                result = true;
            }
        }

        if (cart.isFromAccount()) {
            accountServices.deleteLineItem(deleteLineNum, cart.getOrderNum());
            result = accountServices.saveLineItems(lineItems);
            accountServices.saveCart(cart);
        } else {
            cart.setLineItems(lineItems);
            session.setAttribute("CART", cart);
        }

        return result;
    }

    /**
     * Method to return valid discounts a user
     * can apply to their order
     *
     * @param email a String for the account
     *              that is requesting discounts.
     * @return an ArrayList of valid discounts that can be
     * applied to the order.
     * @author matthewjflee, jjrr1717
     */
    public List<Discount> getValidDiscounts(String email) {
        //get current date for comparison
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        //get all discounts within that time range
        List<String> stringDiscounts = discountRepo.findValidDiscounts(sqlDate, email);
        //list of Discounts
        List<Discount> discounts = new ArrayList<>();

        //parse the comma delimited string returned from query
        for (String s : stringDiscounts) {

            String[] spl = s.split(",");
            float decimalPercent = Float.parseFloat(spl[2]) / 100;
            Discount discount = new Discount(spl[0], spl[1], decimalPercent);
            discounts.add(discount);
        }
        return discounts;
    }

    /**
     * Method to create the discount code and percent json to
     * send to front end.
     *
     * @param session   for the current session
     * @param arrayNode to add the json
     * @return ArrayNode containing the discount code and percent
     * @author jjrr1717
     */
    public ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode) {
        Account currentAccount = (Account) session.getAttribute("ACCOUNT");
        List<Discount> discounts = getValidDiscounts(currentAccount.getEmail());

        for (Discount discount : discounts) {
            ObjectNode node = mapper.createObjectNode();
            node.put("code", discount.getCode());
            node.put("description", discount.getDescription());
            node.put("percent", discount.getPercent());

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
     * @param code    for the discount
     * @param session the current session
     * @author jjrr1717
     */
    public void getSelectedDiscount(String code, HttpSession session) {

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
     *
     * @param cart to view
     * @return ArrayNode containing the information for the cart to view
     * @author jjrr1717, matthewjflee
     */
    public ArrayNode viewCart(Cart cart) {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();
        ArrayNode arrayNode = mapper.createArrayNode();

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
     * @param session user session
     * @return String boolean if account is created successfully for shipping info
     * @throws InvalidInputException   return 503
     * @throws JsonProcessingException exception when parsing json
     * @author matthewjflee, jjrr1717
     */
    public String getShippingInfo(HttpSession session, Account account) throws InvalidInputException, JsonProcessingException {
        if (!account.isUserCreated()) {
            shippingAccount(session, account);
        }

        session.setAttribute("PICKUP", account.isPickup());
        boolean created = accountServices.saveAccount(account);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(created);
    }

    /**
     * Create an account for the customer when they input shipping info and pesrsist in the Accounts table
     *
     * @param session user session
     * @param account customer info
     * @throws InvalidInputException thrown if phone number and postal code is invalid
     * @author matthewjflee
     */
    private void shippingAccount(HttpSession session, Account account) throws InvalidInputException {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();

        if (account.getEmail() == null || account.getFirstName() == null || account.getLastName() == null ||
                account.getPostalCode() == null || account.getPhoneNum() == null) {
            throw new InvalidInputException();
        }

        customerClientUtil.validatePhoneNumber(account.getPhoneNum());
        customerClientUtil.validatePostalCode(account.getPostalCode());
        account.setAccountType('C');

        session.setAttribute("ACCOUNT", account);
        session.setAttribute("TEMP-ACCOUNT", true);
    }

    /**
     * Method to create ArrayNode of all the information that
     * will be displayed on the review order page.
     *
     * @param session       of the current session
     * @param mainArrayNode the node containing all the information
     * @param cart          that contains all the information
     * @return ArrayNode with all the information needed to review the
     * order.
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
        if (discountType.getCode().getCode().equals("NS")) {
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
        if ((boolean) session.getAttribute("PICKUP")) {
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
     *
     * @param session user session
     * @author jjrr1717
     */
    public void decreaseInventory(HttpSession session) {
        //get line items to determine what was sold
        Cart cart = getCart(session);
        List<LineItem> lineItems = cart.getLineItems();

        //loop through the items in the order
        for (LineItem lineItem : lineItems) {
            List<Lot> lotsToUse = obtainSufficientQtyLots(lineItem.getQuantity(), lineItem.getProduct());

            int orderedQty = lineItem.getQuantity();
            for (int i = 0; i < lotsToUse.size() && orderedQty > 0; i++) {
                Lot lot = lotsToUse.get(i);

                int lotQuantity = lot.getQuantity();

                if (lotQuantity >= orderedQty) {
                    lotsToUse.get(i).setQuantity(lotQuantity - orderedQty);
                    lotRepo.save(lot);
                    orderedQty = 0;
                } else {
                    lotsToUse.get(i).setQuantity(0);
                    lotRepo.save(lot);
                    orderedQty -= lotQuantity;
                }
            }
        }
    }


    /**
     * Method to obtain lots that contain
     * enough quantity to fulfill order.
     *
     * @param qty     needed to fulfill order
     * @param product for the order
     * @return List of the lots that can fulfill the order
     * @author jjrr1717
     */
    public List<Lot> obtainSufficientQtyLots(int qty, Product product) {
        //grab lots from database
        List<Lot> lots = lotRepo.findAllLotsWithQuantity(product);
        List<Lot> lotsToUse = new ArrayList<>();

        //get the lots to be used for the order
        for (int i = 0; i < lots.size() && qty > 0; i++) {
            int qtyInLot = lots.get(i).getQuantity();
            int result = qtyInLot - qty;

            lotsToUse.add(lots.get(i));
            if (result >= 0)
                qty = 0;
            else
                qty -= lots.get(i).getQuantity();
        }

        return lotsToUse;
    }

    /**
     * Method to get all the invoice information
     * and save it to the database.
     *
     * @param session user session
     * @return the Invoice saved to the database
     * @author jjrr1717
     */
    public Invoice saveInvoice(HttpSession session) {
        //grab the invoice already generated
        Invoice newInvoice = (Invoice) session.getAttribute("INVOICE");

        //get current date
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        //grab the account created
        Account account = (Account) session.getAttribute("ACCOUNT");

        //get pickup status
        boolean pickup = (boolean) session.getAttribute("PICKUP");

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
     * @param session user session
     * @param invoice the line items belong to
     * @author jjrr1717
     */
    public void saveLineItems(HttpSession session, Invoice invoice) {
        //grab the cart to get the line items
        Cart cart = getCart(session);
        List<LineItem> lineItems = cart.getLineItems();

        cartRepo.save(cart);

        //loop through line items. Assign cart number & invoice number and persist line item
        for (LineItem lineItem : lineItems) {
            lineItem.setInvoice(invoice);
            lineItem.setCart(cart);
            lineItemRepo.save(lineItem);
        }
    }

    /**
     * Apply line items to the invoice.
     * Line items are found in the database
     *
     * @param invoice the invoice to apply
     *                the line items to.
     * @author jjrr1717
     */
    public void applyLineItemsToInvoice(Invoice invoice) {
        //obtain the line items from database
        List<LineItem> lineItems = lineItemRepo.findLineItemByInvoice(invoice);

        //add these to the invoice
        invoice.setLineItemList(lineItems);
    }

    /**
     * Method to get the User information for
     * shipping page if they have an account,
     * or if they are a customer returning
     * to that page.
     *
     * @param account with information to fill fields
     * @param mapper  for the ObjectNode
     * @return Object node for custom json
     */
    public ObjectNode getUserInfo(Account account, ObjectMapper mapper) {
        //create custom json
        ObjectNode node = mapper.createObjectNode();
        node.put("firstName", account.getFirstName());
        node.put("lastName", account.getLastName());
        node.put("email", account.getEmail());
        node.put("phoneNum", account.getPhoneNum());
        node.put("address", account.getAddress());
        node.put("city", account.getCity());
        node.put("province", account.getProvince());
        node.put("postalCode", account.getPostalCode());
        node.put("country", account.getCountry());
        node.put("subscription", account.isSubscript());

        return node;
    }

    /**
     * Method to check if even one item in
     * the cart has something in stock.
     *
     * @param cart to check
     * @return boolean true if there is an item
     * in the cart with stock, otherwise false.
     */
    public boolean checkLineItemStock(Cart cart) {
        boolean inStock = false;

        for (LineItem lineItem : cart.getLineItems()) {
            int quantity = getProductQuantity(lineItem.getProduct().getProdId());

            if (quantity > 0) {
                inStock = true;
            }
        }

        return inStock;
    }

    /**
     * Method to save the discount used on
     * a order to the Account_Discount table
     *
     * @param session for the session
     * @author jjrr1717
     */
    public void applyDiscount(HttpSession session) {
        //get discount from the session
        AccountDiscount accountDiscount = (AccountDiscount) session.getAttribute("ACCOUNT_DISCOUNT");

        if (!accountDiscount.getCode().getCode().equals("NS"))
            accountDiscountRepo.insertWithQuery(accountDiscount);
    }

    /**
     * Transactions after a successful payment occurs
     *
     * @param session user session
     * @throws UnauthorizedException thrown if there is no invoice in the session
     * @throws InvalidInputException postal code or phone number is invalid
     */
    public boolean paymentTransactions(HttpSession session) throws UnauthorizedException, InvalidInputException {
        ///check to ensure all previous steps have been performed
        if (session.getAttribute("INVOICE") == null)
            throw new UnauthorizedException();

        //perform transaction with successful payment
        Invoice newInvoice = saveInvoice(session);
        applyLineItemsToInvoice(newInvoice);
        saveLineItems(session, newInvoice);
        decreaseInventory(session);
        applyDiscount(session);

        //send email to customer/client
        double totalAsDouble = (double) newInvoice.getTotal() / 100;
        emailServices.sendInvoiceEmail(newInvoice.getAccount().getEmail(), newInvoice, totalAsDouble);

        //Create new cart if the user is a client
        Account account = (Account) session.getAttribute("ACCOUNT");
        if (account.isUserCreated())
            accountServices.createNewCart(account);
        else
            session.removeAttribute("CART");

        return true;
    }

}