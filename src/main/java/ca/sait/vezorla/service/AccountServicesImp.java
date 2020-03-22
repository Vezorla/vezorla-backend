package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.repository.InvoiceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServicesImp implements AccountServices{

    private InvoiceRepo invoiceRepo;

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
        return false;
    }

    public boolean validatePaymentInfo() {
        return false;
    }

    /**
     * Method to view an individual invoice
     * from a client's account
     * @param invoiceNum to view
     * @return the invoice to view
     * @author jjrr1717
     */
    public ObjectNode viewInvoice(Long invoiceNum) {
        //obtain the invoice by id
        Optional<Invoice> invoice = invoiceRepo.findById(invoiceNum);

        //create custom json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        String date = invoice.get().getDate() + "";

        node.put("invoiceNum", invoice.get().getInvoiceNum());
        node.put("date", date);
        node.put("lineItems", getLineItemsForInvoice(invoice.get(), node));



        return node;
    }

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

}
