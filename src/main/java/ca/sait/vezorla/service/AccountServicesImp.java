package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.LineItem;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.repository.InvoiceRepo;
import ca.sait.vezorla.repository.LineItemRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServicesImp implements AccountServices {

    private InvoiceRepo invoiceRepo;

    private AccountRepo accountRepo;
    private CartRepo cartRepo;
    private LineItemRepo lineItemRepo;

    /**
     * Find an account by email
     *
     * @author: matthewjflee
     * @param email user's email
     * @return Account
     */
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepo.findById(email);
    }

    public boolean confirmAccount(Long id) {
        return false;
    }

    public Account findById(Long id) {
        return null;
    }

    public List<Invoice> getOrder(Long id) {
        return null;
    }

    public void compareAccounts(Account account1, Account account2) {
        int compare = account1.compareTo(account2);
        System.out.println("compare me "  + compare);
    }

    /**
     * Save the account in the database
     * @param account account to save
     * @return if it was saved
     * @author: matthewjflee
     */
    public boolean saveAccount(Account account, HttpSession session) {
        Account saved = accountRepo.save(account);
        session.setAttribute("ACCOUNT", account);
        return true;
    }

    public void updateAccount(Account account, Account changed) throws InvalidInputException {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();

        if(changed.getEmail() != null)
            account.setEmail(changed.getEmail());

        if(changed.getFirstName() != null)
            account.setFirstName(changed.getFirstName());

        if(changed.getLastName() != null)
            account.setLastName(changed.getLastName());

        if(changed.getPhoneNum() != null) {
            customerClientUtil.validatePhoneNumber(account.getPhoneNum());
            account.setPhoneNum(changed.getPhoneNum());
        }

        if(changed.getAddress() != null)
            account.setAddress(changed.getAddress());

        if(changed.getCity() != null)
            account.setCity(changed.getCity());

        if(changed.getProvince() != null)
            account.setProvince(changed.getProvince());

        if(changed.getPassword() != null)
            account.setPassword(changed.getPassword());

        if(changed.getPostalCode() != null) {
            customerClientUtil.validatePostalCode(account.getPostalCode());
            account.setPostalCode(changed.getPostalCode());
        }

        if(changed.getCountry() != null)
            account.setCountry(changed.getCountry());

        account.setSubscript(changed.isSubscript());
    }

    /**
     * Persist the user's cart in the database
     * @param cart cart to persist
     * @return if it was persisted
     * @author: matthewjflee
     */
    public boolean saveCart(Cart cart) {
//        if (cart.getLineItems().size() > 0)
//            saveLineItems(cart.getLineItems());

        cartRepo.save(cart);
        return true;
    }

    /**
     * Find the most recent cart from the account
     * @param account account to find the cart from
     * @return cart
     * @author: matthewjflee
     */
    public Cart findRecentCart(Account account) {
        Cart cart = cartRepo.findCartByAccount_Email(account.getEmail());
        if (cart == null) {
            cart = new Cart(account);
            List<Cart> carts = cartRepo.findCartsByAccount_Email(account.getEmail());
            carts.add(cart);
            saveCart(cart);
        }

        return cart;
//        return cartRepo.findCartByAccount_Email(email);
    }

    /**
     * Persist line items in the database
     * @param lineItems line items to save
     * @return if it was saved
     * @author: matthewjflee
     */
    public boolean saveLineItems(List<LineItem> lineItems) {
        for (LineItem li : lineItems) {
            lineItemRepo.save(li);
        }

        return true;
    }

    public void saveLineItem(LineItem li) {
        lineItemRepo.save(li);
    }

    /**
     * Delete the line item from the database
     * @param lineNum line item to remove
     * @param cartID cart to delete line item from
     * @author: matthewjflee
     */
    @Transactional
    public void deleteLineItem(Long lineNum, Long cartID) {
        if (lineNum > 0) {
            lineItemRepo.deleteLineItemByLineNumAndCart_OrderNum(lineNum, cartID);
        }
    }

    public Optional<Cart> findCartById(long id) {
        return cartRepo.findById(id);
    }

    public boolean validatePaymentInfo() {
        return false;
    }

    /**
     * Method to view an individual invoice
     * from a client's account
     *
     * @param invoiceNum to view
     * @return the ObjectNode containing invoice information
     * @author jjrr1717
     */
    public ObjectNode viewInvoice(Long invoiceNum, ObjectMapper mapper) {
        CustomerClientUtil ccu = new CustomerClientUtil();
        //obtain the invoice by id
        Optional<Invoice> invoice = invoiceRepo.findById(invoiceNum);

        //create custom json
        ObjectNode node = mapper.createObjectNode();

        String date = invoice.get().getDate() + "";

        //create json for invoice
        node.put("invoiceNum", invoice.get().getInvoiceNum());
        node.put("date", date);
        node.put("lineItems", getLineItemsForInvoice(invoice.get(), node));
        node.put("discount", ccu.formatAmount(invoice.get().getDiscount()));
        node.put("subtotal", ccu.formatAmount(invoice.get().getSubtotal()));
        node.put("taxes", ccu.formatAmount(invoice.get().getTaxes()));
        node.put("total", ccu.formatAmount(invoice.get().getTotal()));


        return node;
    }

    /**
     * Method to get all the line items in an invoice into
     * an ArrayNode. Will be used in viewInvoice() to
     * create the json.
     *
     * @param invoice to access the line items
     * @param node    used to store the ArrayNode of line items.
     * @return ArrayNode to be used in viewInvoice()
     * @author jjrr1717
     */
    public ArrayNode getLineItemsForInvoice(Invoice invoice, ObjectNode node) {
        ArrayNode lineItemNodes = node.arrayNode();
        CustomerClientUtil ccu = new CustomerClientUtil();
        //loop through line items
        for (int i = 0; i < invoice.getLineItemList().size(); i++) {
            ObjectNode lineItem = lineItemNodes.objectNode();
            lineItem.put("name", invoice.getLineItemList().get(i).getCurrentName());
            lineItem.put("price", ccu.formatAmount(invoice.getLineItemList().get(i).getCurrentPrice()));

            //extended price
            long extendedPrice = invoice.getLineItemList().get(i).getQuantity() * invoice.getLineItemList().get(i).getCurrentPrice();
            lineItem.put("extendedPrice", ccu.formatAmount(extendedPrice));
            lineItemNodes.add(lineItem);
        }

        return lineItemNodes;
    }

    /**
     * Method to view the order history on a client's
     * account.
     *
     * @param email  of the client's account.
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     * @author jjrr1717
     */
    public ObjectNode viewOrderHistory(String email, ObjectMapper mapper) {
        CustomerClientUtil ccu = new CustomerClientUtil();

        //obtain all the invoices for account
        List<Invoice> invoices = invoiceRepo.findAllByAccount_Email(email);

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode invoiceNodes = node.arrayNode();

        //loop through invoices to get invoice details
        for (int i = 0; i < invoices.size(); i++) {
            ObjectNode invoiceNode = invoiceNodes.objectNode();
            invoiceNode.put("invoiceNum", invoices.get(i).getInvoiceNum());
            invoiceNode.put("total", ccu.formatAmount(invoices.get(i).getTotal()));
            String date = invoices.get(i).getDate() + "";
            invoiceNode.put("date", date);
            invoiceNodes.add(invoiceNode);
        }

        node.put("invoices", invoiceNodes);

        return node;
    }

    public List<LineItem> getSavedCartLineItems(Cart cart){
        List<LineItem> lineItems = lineItemRepo.findLineItemByOrderNum(cart.getOrderNum());
        return lineItems;
    }
}