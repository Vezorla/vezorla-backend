package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicesImp implements UserServices {

    private ProductRepo productRepo;
    //    private ServletRequestAttributes attr;
//    private HttpServletRequest request;
//    private HttpSession session;
//    ArrayList<LineItem> lineItems = new ArrayList<>();

    public UserServicesImp(ProductRepo productRepo) {
        this.productRepo = productRepo;
//        this.request = request;
//        this.session = request.getSession();
//        this.session = session;
    }

    public void applyDiscount(Discount discount) {

    }

    /**
     * Method to create a new cart object and
     * store it in the session.
     * <p>
     * MAYBE WE DON'T NEED THIS METHOD
     */
    public void createSessionCart() {
//        attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession(true);
//        System.out.println("session new " + session.getId());
        Cart cart = new Cart();
        cart.setLineItems(new ArrayList<LineItem>());
        // this.request.getSession().setAttribute("cart", cart);
    }

    /**
     * Method to get a Cart from the session
     */
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

    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request) {
        //System.out.println("Update session cart " + session.getId());
        Cart cart = (Cart) request.getSession().getAttribute("CART");
        System.out.println("cart me " + cart.getOrderNum());

        if (cart.getOrderNum() == null) { //TODO IS THIS RIGHT???
            System.out.println("cart is null");
            cart = new Cart();
            request.getSession().setAttribute("CART", cart);
        }

        ArrayList<LineItem> lineItems = (ArrayList<LineItem>) cart.getLineItems();
        System.out.println("list size old: " + lineItems.size());
        lineItems.add(lineItem);
        cart.setLineItems(lineItems);
        //System.out.println("After add something to line item list " + lineItemList.size());

        //Fix me later. Grabbing the session
//        attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession();
        request.getSession().setAttribute("CART", cart);

        for(LineItem item : lineItems) {
            System.out.println("item in list" + item.toString());
        }

        System.out.println("list size new: " + lineItems.size());


        return cart;
    }

    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems) {
        //loop through lineItems to get total quantity on order
        int counter = lineItems.stream().mapToInt(LineItem::getQuantity).sum();
        System.out.println("num line items " + counter);
        return counter + "";
    }

    /**
     * Method to return the total quantity in
     * stock per one product id.
     *
     * @param id the id of the product
     * @return an int for the total quantity
     */
    public int getProductQuantity(Long id) {
        Optional<Product> product = productRepo.findById(id);
        int counter = 0;

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

    public LineItem createLineItemSession(Optional<Product> product, String sentQuantity, HttpServletRequest request) {
        LineItem lineItem = new LineItem();
        sentQuantity = sentQuantity.replaceAll("\"", "");
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
