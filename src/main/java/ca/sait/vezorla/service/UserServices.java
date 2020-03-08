package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author jjrr1717, matthewjflee
 */
public interface UserServices {

    void applyDiscount(Discount discount);

    void createLineItems(Long id);

    List<Product> getAllProducts();

    Cart getCart();

    List<Lot> getLots(Long id);

    Optional<Product> getProduct(Long id);

    void getStoreProducts(Long id);

    List<Discount> getValidDiscounts(String email);

    List<Lot> obtainSufficientQtyLots();

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

    void getSelectedDiscount(String code, HttpServletRequest request, HttpSession session);

    ArrayNode viewSessionCart(HttpSession session) throws JsonProcessingException;
}
