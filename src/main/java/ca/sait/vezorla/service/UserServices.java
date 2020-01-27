package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Discount;
import ca.sait.vezorla.model.Lot;
import ca.sait.vezorla.model.Product;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServices {

    public void applyDiscount(Discount discount) {

    }

    public void createLineItems(Long id) {

    }

    public List<Product> getAllProducts() {
        return null;
    }

    public Cart getCart() {
        return null;
    }

    public List<Lot> getLots(Long id) {
        return null;
    }

    public Product getProduct(Long id) {
        return null;
    }

    public void getStoreProducts(Long id) {

    }

    public List<Discount> getValidDiscounts(Date date) {
        return null;
    }

    public List<Lot> obtainSufficientQtyLots() {
        return null;
    }

    public void removeFromCart(Long id) {

    }

    public boolean searchEmail(String email) {
        return false;
    }

    public boolean subscribeEmail(String email) {
        return false;
    }

    public void updateCart(Cart cart) {

    }

}
