package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * UserServices interface.
 *
 * This interface outlines the services as it relates to
 * Authentication.
 *
 * This interface acts as the intermediary between the controllers
 * and the repositories.
 *
 * Services for customers and clients.
 *
 * @author jjrr1717, matthewjflee
 */
public interface UserServices {

    /**
     * Gets all the products to display to the user.
     *
     * @param mapper Object to convert products into JSON.
     * @return Node that contains all the products.
     */
    ArrayNode getAllProducts(ObjectMapper mapper);

    /**
     * Gets a product to display to the user.
     *
     * @param id Product id
     * @param mapper Object to convert products into JSON.
     * @return Node that contains all the products.
     */
    ArrayNode getProduct(Long id, ObjectMapper mapper);

    /**
     * Gets a product to display to the user.
     *
     * @param id Product id
     * @return Optional list that may or may not
     * contain the specified product.
     */
    Optional<Product> getProduct(Long id);

    /**
     * Gets all the valid discounts available to
     * a user.
     *
     * @param email Email of the user
     * @return List of all the available discounts
     */
    List<Discount> getValidDiscounts(String email);

    /**
     * Gets the lots that have enough product
     * to fulfill an order.
     *
     * @param qty Number required to fulfill an order
     * @param product Product to get for the order
     * @return Lost of all the lots that can fulfill
     * the order.
     */
    List<Lot> obtainSufficientQtyLots(int qty, Product product);

    /**
     * Removes a line item from a user's cart.
     *
     * @param id LineItem id to remove
     * @param cart Cart to remove from
     * @param session User's session that holds the cart
     * @return boolean true if removed, false otherwise
     */
    boolean removeLineItem(long id, Cart cart, HttpSession session);

    /**
     * Update a line item in the Cart.
     *
     * @param id LineItem id to remove
     * @param quantity Quantity of the line item
     * @param cart Cart to remove from
     * @param session User's session that holds the cart
     * @return boolean true if updated, false otherwise
     */
    boolean updateLineItem(long id, int quantity, Cart cart, HttpSession session);

    /**
     * Gets a users Cart.
     *
     * @param session User's session to get Cart from
     * @return User's Cart.
     */
    Cart getCart(HttpSession session);

    /**
     * Adds line items to a users cart.
     *
     * @param lineItems LineItem to add.
     * @param cart Cart to add to.
     * @param session User's session to update.
     */
    void addLineItemToSessionCart(List<LineItem> lineItems, Cart cart, HttpSession session);

    /**
     * Gets the product quantity.
     *
     * @param id Product id
     * @return Quantity of product.
     */
    Integer getProductQuantity(Long id);

    /**
     * Gets the total amount of line items in the Cart.
     *
     * @param lineItems List of all LineItems in the Cart.
     * @return Total quantity within the Cart.
     */
    String getTotalCartQuantity(List<LineItem> lineItems);

    /**
     * Validates the order quantity before adding
     * a product to a Cart.
     *
     * @param orderedQuantity Number of products ordered
     * @param inStockQuantity Number of products in stock
     * @return Number of items left in stock after order
     */
    int validateOrderedQuantity(int orderedQuantity, int inStockQuantity);

    /**
     * Creates a line item in a user's Cart.
     *
     * @param product Product to add
     * @param quantity Quantity of product to add
     * @param cart User's Cart to add to
     * @return List of line items for the Cart
     */
    List<LineItem> createLineItem(Product product, int quantity, Cart cart);

    /**
     * Gets the selected discount for the user's order.
     *
     * @param code Discount code
     * @param session User's session to store discount
     */
    void getSelectedDiscount(String code, HttpSession session);

    /**
     * Gets the user's Cart to view.
     *
     * @param cart User's Cart
     * @return ArrayNode Contains the Cart information
     */
    ArrayNode viewCart(Cart cart);

    /**
     * Gets the shipping details of the account.
     *
     * @param session User's session
     * @param account Account to get information from
     * @return String Shipping information
     * @throws InvalidInputException If the shipping information
     * is invalid
     * @throws JsonProcessingException If the shipping information
     * cannot be converted to JSON
     */
    String getShippingInfo(HttpSession session, Account account) throws InvalidInputException, JsonProcessingException;

    /**
     * Creates the discount code and percentage.
     *
     * @param session User's session
     * @param arrayNode Object to convert discount code to
     *                  JSON
     * @return ArrayNode Object to convert discount code to
     * JSON
     */
    ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode);

    /**
     * Gets all the information to be displays on the
     * Review Order Page.
     *
     * @param session User's session
     * @param mainArrayNode Contains all information
     *                      to be displayed
     * @param cart User's Cart
     * @return Contains all information to be displayed
     */
    ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart);

    /**
     * Returns the items from an order to the database
     * that are out of stock.
     *
     * @param cart User's Cart
     * @param session User's session
     * @return Contains all the out of sock items
     */
    ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpSession session);

    /**
     * Decreases the product quantity in the inventory
     * for all the items in a user's Cart.
     *
     * @param session User's session
     */
    void decreaseInventory(HttpSession session);

    /**
     * Saves the order Invoice to the database.
     *
     * @param session User's session
     * @return Invoice to be saved
     */
    Invoice saveInvoice(HttpSession session);

    /**
     * Saves the Cart and LineItems to the database.
     *
     * @param session User's session
     * @param invoice Invoice to be saved
     */
    void saveLineItems(HttpSession session, Invoice invoice);

    /**
     * Apply LineItems from the Cart to the Invoice.
     *
     * @param invoice Invoice to be applied to
     */
    void applyLineItemsToInvoice(Invoice invoice);

    /**
     * Gets all the user's shipping information.
     *
     * Note: only gets this information if the user
     * has an account or are a returning customer.
     *
     * @param account User's account
     * @param mapper Contains all the users shipping
     *               information
     * @return Contains all the users shipping information
     * in JSON
     */
    ObjectNode getUserInfo(Account account, ObjectMapper mapper);

    /**
     * Checks to see if a line item in the Cart is
     * in stock.
     *
     * @param cart User's cart
     * @return boolean true if in stock, false otherwise
     */
    boolean checkLineItemStock(Cart cart);

    /**
     * Apply the session discount to the order.
     *
     * @param session User's session
     */
    void applyDiscount(HttpSession session);

    /**
     * Contains all the methods that will be called when a
     * successful transaction occurs.
     *
     * Set Invoice to null
     * Save the invoice
     * decrease the inventory
     * send email to user
     * create a new cart
     *
     * @param session User's session
     * @return boolean true if all methods executed, false otherwise
     * @throws UnauthorizedException If a user is not authorized
     * @throws InvalidInputException If transaction information is invalid
     */
    boolean paymentTransactions(HttpSession session) throws UnauthorizedException, InvalidInputException;

    /**
     * Checks to see if a LineItem is in stock.
     *
     * @param id LineItem id
     * @param updatedQty New quantity
     * @return boolean true if in stock, false otherwise
     */
    boolean checkIfLineItemInStock(long id, int updatedQty);

    /**
     * Gets the banner message for the Home page.
     *
     * @param mapper Used for JSON conversion
     * @return Contains the banner message
     */
    ObjectNode getBannerMessage(ObjectMapper mapper);

    /**
     * Gets the Top Product Sold.
     *
     * @param mapper Used for JSON conversion
     * @return Contains the Top Product Sold information
     */
    ObjectNode getTopProduct(ObjectMapper mapper);
}
