package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

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
    private AccountServices accountServices;
    private EmailServices emailServices;
    private ObjectMapper mapper;

    /**
     * Method to save the discount used on
     * a order to the Account_Discount table
     *
     * @param request for the session
     * @author jjrr1717
     */
    public void applyDiscount(HttpServletRequest request) {
        //get discount from the session
        HttpSession session = request.getSession();
        AccountDiscount accountDiscount = (AccountDiscount) session.getAttribute("ACCOUNT_DISCOUNT");

        if (!accountDiscount.getCode().getCode().equals("NS")) {
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
     * @param request   user's request
     * @return the cart with new line item added
     * @author matthewjflee, jjrr1717
     */
    public Cart updateSessionCart(List<LineItem> lineItems, Cart cart, HttpServletRequest request) {
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
     * @param orderedQuantitySent the quantity wanted to add to the line item
     * @param inStockQuantity     quantity currently in stock in database
     * @return the difference. >=0 means there is enough stock. >0 means
     * there is not enough stock.
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
     *
     * @param cart    to check if any items are out of stock
     * @param request for the session
     * @return an ArrayNode of all the out of stock items for front end
     * to use.
     * @author jjrr1717
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
     * @param product      for the line item
     * @param sentQuantity quantity for the line item
     * @param cart         user's cart
     * @return line item to be added to the session
     * @author matthewjflee, jjrr1717
     */
//    public ArrayList<LineItem> createLineItemSession(Optional<Product> product, String sentQuantity, Cart cart) {
    public List<LineItem> createLineItemSession(Optional<Product> product, String sentQuantity, Cart cart) {
        //Get cart
        LineItem lineItem;
//        ArrayList<LineItem> lineItems;
        List<LineItem> lineItems;

        //Parse quantity
        sentQuantity = sentQuantity.replaceAll("\"", "");
        int quantity = Integer.parseInt(sentQuantity);

        //check if product already exists
        int lineItemIndex = checkProductLineItem(product.get().getProdId(), cart);
        if (lineItemIndex == -1) {
            //Create line item
            lineItem = new LineItem(quantity, product.get().getName(), product.get().getPrice(), cart, product.get());
//            lineItems = (ArrayList<LineItem>) cart.getLineItems();
            lineItems = cart.getLineItems();
            lineItems.add(lineItem);
        } else {
            lineItems = updateLineItemAdd(quantity, cart, lineItemIndex);
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
        for (int i = 0; i < cart.getLineItems().size(); i++) {
            if (cart.getLineItems().get(i).getProduct().getProdId().equals(id)) {
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
    private List<LineItem> updateLineItemAdd(int quantity, Cart cart, int index) {
        //get cart with line items
        List<LineItem> lineItems = cart.getLineItems();

        //Set quantity
        int currentQuantity = lineItems.get(index).getQuantity();
        lineItems.get(index).setQuantity(currentQuantity + quantity);

//        cart.setLineItems(lineItems);

        return lineItems;
    }

    /**
     * Update line item quantity in cart.
     * At the review page.
     *
     * @param id       of the product to update
     * @param quantity of the line item
     * @param cart     to add updated line item
     * @author matthewjflee, jjrr1717
     */
    public boolean updateLineItemSession(long id, int quantity, Cart cart, HttpServletRequest request) {
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

        if(cart.isFromAccount()) {
            if(lineItem != null)
                accountServices.saveLineItem(lineItem);
        }
        else
            request.getSession().setAttribute("CART", cart);

        return result;
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
    public boolean removeLineItemSession(long id, Cart cart, HttpSession session) {
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
     * Create and persist an account in the Accounts table
     *
     * @param account to persist in database
     * @return boolean true if it was successfully added, otherwise false
     * @author matthewjflee, jjrr1717
     */
    public boolean saveAccount(Account account) {
        Account saved = accountRepo.save(account);
        return true;
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepo.findById(email);
    }

    /**
     * Return all products in the Products table
     *
     * @return
     * @author jjrr1717
     */
    public ArrayNode getAllProducts(ObjectMapper mapper) {
        CustomerClientUtil ccu = new CustomerClientUtil();

        //obtain all the products
        ArrayList<Product> products = (ArrayList<Product>) productRepo.findAll();

        //create custom json
        ArrayNode productsNode = mapper.createArrayNode();

        //loop through products
        for (int i = 0; i < products.size(); i++) {
            ObjectNode node = productsNode.objectNode();
            node.put("prodId", products.get(i).getProdId());
            node.put("name", products.get(i).getName());
            node.put("description", products.get(i).getDescription());
            node.put("subdescription", products.get(i).getSubdescription());
            node.put("harvestTime:", products.get(i).getHarvestTime());
            node.put("imageMain", products.get(i).getImageMain());
            node.put("imageOne", products.get(i).getImageOne());
            node.put("imageTwo", products.get(i).getImageTwo());
            node.put("imageThree", products.get(i).getImageThree());
            node.put("active", products.get(i).isActive());
            node.put("threshold", products.get(i).getThreshhold());
            node.put("price", ccu.formatAmount(products.get(i).getPrice()));
            node.put("oldPrice", ccu.formatAmount(products.get(i).getOldPrice()));
            productsNode.add(node);
        }

        return productsNode;
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
     * @param id product ID
     * @return product
     * @author matthewjflee, jjrr1717
     */
    public ArrayNode getProduct(Long id, ObjectMapper mapper) {

        CustomerClientUtil ccu = new CustomerClientUtil();

        //obtain all the products
        Optional<Product> product = productRepo.findById(id);

        //create custom json
        ArrayNode productsNode = mapper.createArrayNode();

        ObjectNode node = productsNode.objectNode();
        node.put("prodId", product.get().getProdId());
        node.put("name", product.get().getName());
        node.put("description", product.get().getDescription());
        node.put("subdescription", product.get().getSubdescription());
        node.put("harvestTime:", product.get().getHarvestTime());
        node.put("imageMain", product.get().getImageMain());
        node.put("imageOne", product.get().getImageOne());
        node.put("imageTwo", product.get().getImageTwo());
        node.put("imageThree", product.get().getImageThree());
        node.put("active", product.get().isActive());
        node.put("threshold", product.get().getThreshhold());
        node.put("price", ccu.formatAmount(product.get().getPrice()));
        node.put("oldPrice", ccu.formatAmount(product.get().getOldPrice()));
        productsNode.add(node);


        return productsNode;
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
     * @param code    for the discount
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
     *
     * @param request the session
     * @param cart    to view
     * @return ArrayNode containing the information for the cart to view
     * @throws JsonProcessingException error when parsing JSON
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
     * @param session user session
     * @return String boolean if account is created successfully for shipping info
     * @throws InvalidInputException   return 503
     * @throws JsonProcessingException exception when parsing json
     * @author: matthewjflee, jjrr1717
     */
    public String getShippingInfo(HttpSession session, Account account) throws InvalidInputException, JsonProcessingException {
        if(!account.isUserCreated())
            shippingAccount(session, account);

        session.setAttribute("PICKUP", account.isPickup());
        boolean created = saveAccount(account);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(created);
    }

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
        boolean created = saveAccount(account);

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
     * @param request the session
     * @author jjrr1717
     */
    public void decreaseInventory(HttpServletRequest request) {
        //get line items to determine what was sold
        HttpSession session = request.getSession();
        Cart cart = getCart(session);
        List<LineItem> lineItems = cart.getLineItems();

        //loop through the items in the order
        for (int i = 0; i < lineItems.size(); i++) {
            List<Lot> lotsToUse = obtainSufficientQtyLots(lineItems.get(i).getQuantity(), lineItems.get(i).getProduct());

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
     * @param request for the session
     * @param invoice the line items belong to
     * @author jjrr1717
     */
    public void saveLineItems(HttpServletRequest request, Invoice invoice) {
        HttpSession session = request.getSession();

        //grab the cart to get the line items
        Cart cart = getCart(session);
        List<LineItem> lineItems = cart.getLineItems();

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
     * @param mapper for the ObjectNode
     * @return Object node for custom json
     */
    public ObjectNode getUserInfo(Account account, ObjectMapper mapper){
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
     * @param cart to check
     * @return boolean true if there is an item
     * in the cart with stock, otherwise false.
     */
    public boolean checkLineItemStock(Cart cart){
        boolean inStock = false;

        for(LineItem lineItem : cart.getLineItems()){
            int quantity = getProductQuantity(lineItem.getProduct().getProdId());

            if(quantity > 0){
                inStock = true;
            }
        }

        return inStock;
    }

    /**
     * Transactions after a successful payment occurs
     *
     * @param request for the session
     * @throws UnauthorizedException
     * @throws InvalidInputException
     */
    public void paymentTransactions(HttpServletRequest request) throws UnauthorizedException, InvalidInputException {
        HttpSession session = request.getSession();

        ///check to ensure all previous steps have been performed
        if (session.getAttribute("INVOICE") == null) {
            throw new UnauthorizedException();
        }

        //perform transaction with successful payment
        Invoice newInvoice = saveInvoice(request);
        applyLineItemsToInvoice(newInvoice);
        saveLineItems(request, newInvoice);
        decreaseInventory(request);
        applyDiscount(request);

        //send email to customer/client
        double totalAsDouble = (double) newInvoice.getTotal() / 100;
        emailServices.sendInvoiceEmail(newInvoice.getAccount().getEmail(), newInvoice, totalAsDouble);

        //Create new cart if the user is a client
        Account account = (Account) session.getAttribute("ACCOUNT");
        if (account.isUserCreated())
            accountServices.createNewCart(account);
        else
            request.getSession().removeAttribute("CART");
    }

}