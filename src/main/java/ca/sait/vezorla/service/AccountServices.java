package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface AccountServices {

    boolean confirmAccount(Long id);

    List<Invoice> getOrder(Long id);

    boolean saveAccount(Account account);

    boolean saveCart(Cart cart);

    boolean validatePaymentInfo();

    ObjectNode viewInvoice(Long invoiceNum, ObjectMapper mapper);

    ObjectNode viewOrderHistory(String email, ObjectMapper mapper);
}
