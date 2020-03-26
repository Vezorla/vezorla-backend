package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
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

    void applyDiscount(HttpServletRequest request);

    ArrayNode getAllProducts(ObjectMapper mapper);

    Cart getCart();

    List<Lot> getLots(Long id);

    ArrayNode getProduct(Long id, ObjectMapper mapper);

    Optional<Product> getProduct(Long id);

    void getStoreProducts(Long id);

    List<Discount> getValidDiscounts(String email);

    List<Lot> obtainSufficientQtyLots(int qty, Product product);

    boolean removeLineItemSession(long id, Cart cart, HttpSession session);

    boolean updateLineItemSession(long id, int quantity, Cart cart, HttpServletRequest request);

    Cart getSessionCart(HttpSession session);

    Cart updateSessionCart(List<LineItem> lineItems, Cart cart, HttpServletRequest request);

    int getProductQuantity(Long id);

    String getTotalCartQuantity(List<LineItem> lineItems);

    int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity);

    List<LineItem> createLineItemSession(Optional<Product> product, String quantity, Cart cart);
//    List<LineItem> createLineItemSession(Optional<Product> product, String quantity, Cart cart);

    boolean saveAccount(Account account);

    Optional<Account> findAccountByEmail(String email);

    void getSelectedDiscount(String code, HttpServletRequest request, HttpSession session);

    ArrayNode viewSessionCart(HttpServletRequest request, Cart cart) throws JsonProcessingException;

    String getShippingInfo(HttpSession session, Account account) throws InvalidInputException, JsonProcessingException;

    ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode);

    ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart);

    ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpServletRequest request);

    void decreaseInventory(HttpServletRequest request);

    Invoice saveInvoice(HttpServletRequest request);

    void saveLineItems(HttpServletRequest request, Invoice invoice);

    void applyLineItemsToInvoice(Invoice invoice);

    ObjectNode getUserInfo(Account account, ObjectMapper mapper);

    boolean checkLineItemStock(Cart cart);
}
