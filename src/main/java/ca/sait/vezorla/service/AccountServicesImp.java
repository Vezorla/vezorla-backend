package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.repository.AccountRepo;
import ca.sait.vezorla.repository.CartRepo;
import ca.sait.vezorla.repository.InvoiceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServicesImp implements AccountServices{

    private InvoiceRepo invoiceRepo;
    private AccountRepo accountRepo;
    private CartRepo cartRepo;

    public boolean confirmAccount(Long id) {
        return false;
    }

    public Account findById(Long id) {
        return null;
    }

    public List<Invoice> getOrder(Long id) {
        return null;
    }

    public boolean saveAccount(Account account) {
        Account saved = accountRepo.save(account);
        return true;
    }

    public boolean saveCart(Cart cart) {
        cartRepo.save(cart);
        return true;
    }

    public Cart findRecentCart(String email) {
        return cartRepo.findCartByAccount_Email(email);
    }

    public long findRecentCartID(String email) {
        return cartRepo.findCartIdByAccount_Email(email);
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
     * @param node used to store the ArrayNode of line items.
     * @return ArrayNode to be used in viewInvoice()
     * @author jjrr1717
     */
    public ArrayNode getLineItemsForInvoice(Invoice invoice, ObjectNode node){
        ArrayNode lineItemNodes = node.arrayNode();
        CustomerClientUtil ccu = new CustomerClientUtil();
        //loop through line items
        for(int i = 0; i < invoice.getLineItemList().size(); i++){
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
     * @param email of the client's account.
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     * @author jjrr1717
     */
    public ObjectNode viewOrderHistory(String email, ObjectMapper mapper){
        CustomerClientUtil ccu = new CustomerClientUtil();

        //obtain all the invoices for account
        ArrayList<Invoice> invoices = (ArrayList<Invoice>) invoiceRepo.findAllByAccount_Email(email);

        //create custom json
        ObjectNode node = mapper.createObjectNode();
        ArrayNode invoiceNodes = node.arrayNode();

        //loop through invoices to get invoice details
        for(int i = 0; i < invoices.size(); i++){
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

}