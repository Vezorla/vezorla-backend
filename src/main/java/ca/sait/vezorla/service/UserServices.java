package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author jjrr1717, matthewjflee
 */
public interface UserServices {

    public void applyDiscount(Discount discount);

    public void createLineItems(Long id);

    public List<Product> getAllProducts();

    public Cart getCart();

    public List<Lot> getLots(Long id);

    public Optional<Product> getProduct(Long id);

    public void getStoreProducts(Long id);

    public List<Discount> getValidDiscounts();

    public List<Lot> obtainSufficientQtyLots();

    public boolean removeLineItemSession(Long id, Cart cart, HttpServletRequest request);

    public boolean searchEmail(String email);

    public boolean subscribeEmail(String email);

    public boolean updateLineItemSession(Long id, int quantity, Cart cart, HttpServletRequest request);

    public Cart getSessionCart(HttpSession session);

    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request);

    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems);

    public int getProductQuantity(Long id);

    public void createLineItems(Product product);

    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity);

    public LineItem createLineItemSession(Optional<Product> product, String quantity, HttpServletRequest request);

    public boolean saveAccount(Account account);
}
