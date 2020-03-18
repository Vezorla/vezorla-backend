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

    public void applyDiscount(HttpServletRequest request);

    public void createLineItems(Long id);

    public List<Product> getAllProducts();

    public Cart getCart();

    public List<Lot> getLots(Long id);

    public Optional<Product> getProduct(Long id);

    public void getStoreProducts(Long id);

    public List<Discount> getValidDiscounts(String email);

    public List<Lot> obtainSufficientQtyLots(int qty, Product product);

    public boolean removeLineItemSession(Long id, Cart cart, HttpServletRequest request);

    public boolean searchEmail(String email);

    public boolean subscribeEmail(String email);

    public boolean updateLineItemSession(Long id, int quantity, Cart cart, HttpServletRequest request);

    public Cart getSessionCart(HttpSession session);

    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request);

    public String getTotalCartQuantity(ArrayList<LineItem> lineItems);

    public int getProductQuantity(Long id);

    public void createLineItems(Product product);

    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity);

    public LineItem createLineItemSession(Optional<Product> product, String quantity, HttpServletRequest request);

    public boolean saveAccount(Account account);

    public void getSelectedDiscount(String code, HttpServletRequest request, HttpSession session);

    public ArrayNode viewSessionCart(HttpServletRequest request, Cart cart) throws JsonProcessingException;

    public String getShippingInfo(HttpServletRequest request, Account account) throws InvalidInputException, JsonProcessingException;

    public ArrayNode buildValidDiscounts(HttpSession session, ArrayNode arrayNode);

    public ArrayNode reviewOrder(HttpSession session, ArrayNode mainArrayNode, Cart cart);

    public ArrayNode checkItemsOrderedOutOfStock(Cart cart, HttpServletRequest request);

    public void decreaseInventory(HttpServletRequest request);

    public Invoice saveInvoice(HttpServletRequest request);

    public void saveLineItems(HttpServletRequest request, Invoice invoice);
}
