package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.HttpEntity;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author jjrr1717, matthewjflee
 */
public interface UserServices {

    void applyDiscount(HttpServletRequest request);

    void createLineItems(Long id);

    List<Product> getAllProducts();

    Cart getCart();

    List<Lot> getLots(Long id);

    Optional<Product> getProduct(Long id);

    void getStoreProducts(Long id);

    List<Discount> getValidDiscounts(String email);

    List<Lot> obtainSufficientQtyLots(int qty, Product product);

    boolean removeLineItemSession(Long id, Cart cart, HttpServletRequest request);

    boolean searchEmail(String email);

    boolean subscribeEmail(String email);

    boolean updateLineItemSession(Long id, int quantity, Cart cart, HttpServletRequest request);

    Cart getSessionCart(HttpSession session);

    Cart updateSessionCart(LineItem lineItem, HttpServletRequest request);

    String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems);

    int getProductQuantity(Long id);

    void createLineItems(Product product);

    int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity);

    LineItem createLineItemSession(Optional<Product> product, String quantity, HttpServletRequest request);

    boolean createAccount(Account account);

    Optional<Account> findAccount(String email);

    ArrayNode viewSessionCart(HttpServletRequest request, Cart cart) throws JsonProcessingException;

    String getShippingInfo(HttpServletRequest request, Account account) throws InvalidInputException, JsonProcessingException;

    ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode);

    ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart);

    ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpServletRequest request);

    void decreaseInventory(HttpServletRequest request);

    Invoice saveInvoice(HttpServletRequest request);
}
