package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.OutOfStockException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * @author jjrr1717, matthewjflee
 */
public interface UserServices {

    ArrayNode getAllProducts(ObjectMapper mapper);

    ArrayNode getProduct(Long id, ObjectMapper mapper);

    Optional<Product> getProduct(Long id);

    List<Discount> getValidDiscounts(String email);

    List<Lot> obtainSufficientQtyLots(int qty, Product product);

    boolean removeLineItem(long id, Cart cart, HttpSession session);

    boolean updateLineItem(long id, int quantity, Cart cart, HttpSession session);

    Cart getCart(HttpSession session);

    void addLineItemToSessionCart(List<LineItem> lineItems, Cart cart, HttpSession session);

    int getProductQuantity(Long id);

    String getTotalCartQuantity(List<LineItem> lineItems);

    int validateOrderedQuantity(int orderedQuantity, int inStockQuantity);

    List<LineItem> createLineItem(Product product, int quantity, Cart cart);

    void getSelectedDiscount(String code, HttpSession session);

    ArrayNode viewCart(Cart cart);

    String getShippingInfo(HttpSession session, Account account) throws InvalidInputException, JsonProcessingException;

    ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode);

    ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart);

    ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpSession session);

    void decreaseInventory(HttpSession session);

    Invoice saveInvoice(HttpSession session);

    void saveLineItems(HttpSession session, Invoice invoice);

    void applyLineItemsToInvoice(Invoice invoice);

    ObjectNode getUserInfo(Account account, ObjectMapper mapper);

    boolean checkLineItemStock(Cart cart);

    void applyDiscount(HttpSession session);

    boolean paymentTransactions(HttpSession session) throws UnauthorizedException, InvalidInputException;

    boolean checkIfLineItemInStock(long id, int updatedQty);

    ObjectNode getBannerMessage(ObjectMapper mapper);
}
