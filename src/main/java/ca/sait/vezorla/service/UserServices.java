package ca.sait.vezorla.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ca.sait.vezorla.model.*;

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
    public Cart getSessionCart();
    public void createSessionCart();
    public Cart updateSessionCart(LineItem lineItem);
    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems);
}
