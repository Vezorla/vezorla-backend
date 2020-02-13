package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserServices {

    public void applyDiscount(Discount discount) ;
    public void createLineItems(Long id) ;
    public List<Product> getAllProducts();
    public Cart getCart();
    public List<Lot> getLots(Long id);
    public Optional<Product> getProduct(Long id);
    public void getStoreProducts(Long id);
    public List<Discount> getValidDiscounts(Date date);
    public List<Lot> obtainSufficientQtyLots();
    public void removeFromCart(Long id);
    public boolean searchEmail(String email);
    public boolean subscribeEmail(String email);
    public void updateCart(Cart cart);
    public Cart getSessionCart(HttpServletRequest request);
    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request);
    public void createSessionCart(HttpServletRequest request);
    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems);
    public int getProductQuantity(Long id);
    public void createLineItems(Product product);
    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity);

    public LineItem createLineItemSession(Optional<Product> product, String quantity, HttpServletRequest request);
}
