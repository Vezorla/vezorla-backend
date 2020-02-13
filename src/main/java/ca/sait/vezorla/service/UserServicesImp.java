package ca.sait.vezorla.service;

import ca.sait.vezorla.model.*;
import ca.sait.vezorla.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
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
    private ServletRequestAttributes attr;
    private HttpServletRequest request;
    private HttpSession session;

    public UserServicesImp(ProductRepo productRepo, HttpServletRequest request) {
        this.productRepo = productRepo;
        this.request = request;
        this.session = request.getSession();
    }

    public void applyDiscount(Discount discount) {

    }

    /**
     * Method to create a new cart object and
     * store it in the session.
     */
    public void createSessionCart(HttpServletRequest request) {
//        attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession(true);
//        System.out.println("session new " + session.getId());
        Cart cart = new Cart();
        cart.setLineItems(new ArrayList<LineItem>());
        request.getSession().setAttribute("cart", cart);
    }

    /**
     * Method to get a Cart from the session
     */
    public Cart getSessionCart(HttpServletRequest request) {
//        attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession();
        HttpSession session = request.getSession();

        System.out.println("cart session " + session.getId());
        Cart cart = null;
        if (session.getAttribute("cart") == null) {
            createSessionCart(request);
        } else {
            System.out.println("session existing " + session.getId());
            cart = (Cart) session.getAttribute("cart");
        }
        return cart;
    }

    public Cart updateSessionCart(LineItem lineItem, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println();
        Cart cart = getSessionCart(request);
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) cart.getLineItems();
        lineItemList.add(lineItem);
        cart.setLineItems(lineItemList);

        //Fix me later. Grabbing the session
//        attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession();
        session.setAttribute("cart", cart);
        return cart;
    }

    public String getTotalSessionCartQuantity(ArrayList<LineItem> lineItems) {
        //loop through lineItems to get total quantity on order
        int counter = lineItems.stream().mapToInt(LineItem::getQuantity).sum();
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

        System.out.println("DB Quant " + quantity);

        return quantity;
    }

    public int validateOrderedQuantity(String orderedQuantitySent, int inStockQuantity){
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
        lineItem.setCart(getSessionCart(request));
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
