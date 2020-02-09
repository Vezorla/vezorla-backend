package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicesImp implements UserServices {

    private ProductRepo productRepo;
    private ServletRequestAttributes attr;

    public UserServicesImp(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public void applyDiscount(Discount discount) {

    }

    /**
     * Method to create a new cart object and
     * store it in the session.
     */
    public void createSessionCart() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        Cart cart = new Cart();
        session.setAttribute("cart", cart);
    }


    /**
     * Method to get a Cart from the session
     */
    public Cart getSessionCart() {
        attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        Cart cart = null;
        if (session.getAttribute("cart") == null) {
            createSessionCart();
        } else {
            cart = (Cart) session.getAttribute("cart");
        }
        return cart;
    }

    public Cart updateSessionCart(LineItem lineItem) {
        Cart cart = getSessionCart();
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) cart.getLineItems();
        lineItemList.add(lineItem);
        cart.setLineItems(lineItemList);
        return cart;
    }

    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems){
        //loop through lineItems to get total quantity on order
        int counter = lineItems.stream().mapToInt(LineItem::getQuantity).sum();

        return counter + "";
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

    public Optional<Product> getProduct(Long id) { //It wanted Optional


        return productRepo.findById(id);
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
