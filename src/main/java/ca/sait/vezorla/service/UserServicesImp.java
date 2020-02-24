package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.ProductRepo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicesImp implements UserServices {

    private ProductRepo productRepo;

    public UserServicesImp(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public void applyDiscount(Discount discount) {

    }

    public Cart getSessionCart(HttpSession session) {
        System.out.println("cart session " + session.getId());
        Cart cart = (Cart) session.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
        } else {
            System.out.println("session existing " + session.getId());
        }
        return cart;
    }

    /**
     * Adding line item to cart
     * @param lineItem
     * @param request
     * @return
     */
    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute("CART");

        if (cart == null) {
            System.out.println("cart is null");
            cart = new Cart();
            request.getSession().setAttribute("CART", cart);
        }

        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();
        System.out.println("list size old: " + lineItems.size());
        lineItems.add(lineItem);
        System.out.println("new size " + lineItems.size());
        cart.setLineItems(lineItems);
        request.getSession().setAttribute("CART", cart);

        return cart;
    }

    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems) {
        //loop through lineItems to get total quantity on order
        int counter = lineItems.stream().mapToInt(LineItem::getQuantity).sum();
        System.out.println("num line items " + counter);
        return counter + "";
    }

    /**
     * Get the total quantity of the product from the Products database
     * @param id
     * @return
     */
    public int getProductQuantity(Long id) {
//        Optional<Product> product = productRepo.findById(id);
//        int counter = 0;
//        for (int i = 0; i < product.get().getLotList().size(); i++) {
//            counter += product.get().getLotList().get(i).getQuantity();
//        }
        int quantity = productRepo.findTotalQuantity(id);
        System.out.println("DB total quantity " + quantity);

        return quantity;
    }

    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity) {
        orderedQuantitySent = orderedQuantitySent.replaceAll("\"", "");
        int orderedQuantity = Integer.parseInt(orderedQuantitySent);
        return inStockQuantity - orderedQuantity;
    }

    @Override
    public void createLineItems(Product product) {

    }

    /**
     * Create a line item from the product
     * @param product
     * @param sentQuantity
     * @param request
     * @return
     */
    public LineItem createLineItemSession(Optional<Product> product, String sentQuantity, HttpServletRequest request) {
        LineItem lineItem = new LineItem();
        sentQuantity = sentQuantity.replaceAll("\"", ""); //Sending a string so replace \
        int quantity = Integer.parseInt(sentQuantity);

        lineItem.setQuantity(quantity);
        lineItem.setCurrentName(product.get().getName());
        lineItem.setCurrentPrice(product.get().getPrice());
        lineItem.setCart((Cart) request.getSession().getAttribute("CART"));
        lineItem.setProduct(product.get());

        System.out.println("create line item session " + request.getSession().getId());
        System.out.println("Line Item Name " + lineItem.getCurrentName());
        return lineItem;
    }

    /**
     * Update line item quantity in cart
     * @param id
     * @param quantity
     * @param cart
     */
    public boolean updateLineItem(Long id, int quantity, Cart cart, HttpServletRequest request) {
        boolean result = false;
        ArrayList<LineItem> lineItems = (ArrayList) cart.getLineItems();
        for(int i = 0; i < lineItems.size(); i++) {
            if(lineItems.get(i).getProduct().getProdId().equals(id)) {
                lineItems.get(i).setQuantity(quantity);
                result = true;
            }
        }
        request.getSession().setAttribute("CART", cart);

        return result;

    }

    public void createLineItems(Long id) {

    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productRepo.findAll());
    }

    public Cart getCart() {
        return null;
    }

    public List<Lot> getLots(Long id) {
        return null;
    }

    public Optional<Product> getProduct(Long id) { //It wanted Optionals
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

}
