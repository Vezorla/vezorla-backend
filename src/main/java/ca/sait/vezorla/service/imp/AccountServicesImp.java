package ca.sait.vezorla.service.imp;

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
import ca.sait.vezorla.service.AccountServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
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
     * @param email user's email
     * @return Account the account
     * @author matthewjflee
     */
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepo.findById(email);
    }

    public boolean confirmAccount(Long id) {
        return false;
    }

    /**
     * Create and persist an account in the Accounts table
     *
     * @param account to persist in database
     * @return boolean true if it was successfully added, otherwise false
     * @author matthewjflee, jjrr1717
     */
    public boolean saveAccount(Account account) {
        accountRepo.save(account);
        return true;
    }

    /**
     * Save the account in the database
     * @param account account to save
     * @return if it was saved
     * @author matthewjflee
     */
    public boolean saveAccount(Account account, HttpSession session) {
        accountRepo.save(account);
        session.setAttribute("ACCOUNT", account);
        return true;
    }

    /**
     * Update the existing account with the fields
     * The reason why this method has so many checks is if this is not done, it will replace the field with null
     * in the accounts table
     * @param account Account to be updated
     * @param changed new info
     * @return the account after updating
     * @throws InvalidInputException if the postal code or phone number is invalid
     * @author matthewjflee
     */
    public Account updateAccount(Account account, Account changed) throws InvalidInputException {
        CustomerClientUtil customerClientUtil = new CustomerClientUtil();

        if(changed.getEmail() != null)
            account.setEmail(changed.getEmail());

        if(changed.getFirstName() != null)
            account.setFirstName(changed.getFirstName());

        if(changed.getLastName() != null)
            account.setLastName(changed.getLastName());

        if(changed.getPhoneNum() != null) {
            customerClientUtil.validatePhoneNumber(changed.getPhoneNum());
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
            customerClientUtil.validatePostalCode(changed.getPostalCode());
            account.setPostalCode(changed.getPostalCode());
        }

        if(changed.getCountry() != null)
            account.setCountry(changed.getCountry());

        if(changed.getIsSubscript() != null)
            account.setIsSubscript(changed.getIsSubscript());

        return account;
    }

    /**
     * Persist the user's cart in the database
     * @param cart cart to persist
     * @return if it was persisted
     * @author matthewjflee
     */
    public boolean saveCart(Cart cart) {
        cartRepo.save(cart);
        return true;
    }

    /**
     * Find the most recent cart from the account
     * @param account account to find the cart from
     * @return cart
     * @author matthewjflee
     */
    public Cart findRecentCart(Account account) {
        Cart cart = cartRepo.findCartByAccountEmail(account.getEmail());
        if (cart == null)
            cart = createNewCart(account);

        return cart;
    }

    /**
     * Create a new cart for a client
     * @param account account to create new cart for
     * @return cart
     * @author matthewjflee
     */
    public Cart createNewCart(Account account) {
        Cart cart = new Cart(account);
        List<Cart> carts = cartRepo.findCartsByAccountEmail(account.getEmail());
        carts.add(cart);
        saveCart(cart);

        return cart;
    }

    /**
     * Persist line items in the database
     * @param lineItems line items to save
     * @return if it was saved
     * @author matthewjflee
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
     * @author matthewjflee
     */
    @Transactional
    public void deleteLineItem(Long lineNum, Long cartID) {
        if (lineNum > 0) {
            lineItemRepo.deleteLineItemByLineNumAndCart_OrderNum(lineNum, cartID);
        }
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
     * create the json.s
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
            lineItem.put("qty", invoice.getLineItemList().get(i).getQuantity());
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
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     * @author jjrr1717
     */
    public ObjectNode viewOrderHistory(ObjectMapper mapper, HttpSession session) {
        CustomerClientUtil ccu = new CustomerClientUtil();

        //get account email
        Account account = (Account) session.getAttribute("ACCOUNT");

        //obtain all the invoices for account
        List<Invoice> invoices = invoiceRepo.findAllByAccountEmail(account.getEmail());

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode invoiceNodes = node.arrayNode();

        //loop through invoices to get invoice details
        for (Invoice invoice : invoices) {
            ObjectNode invoiceNode = invoiceNodes.objectNode();
            invoiceNode.put("invoiceNum", invoice.getInvoiceNum());
            invoiceNode.put("total", ccu.formatAmount(invoice.getTotal()));
            String date = invoice.getDate() + "";
            invoiceNode.put("date", date);
            invoiceNodes.add(invoiceNode);
        }

        node.put("invoices", invoiceNodes);

        return node;
    }
}