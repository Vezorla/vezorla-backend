package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.LineItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AccountServices {

    boolean confirmAccount(Long id);

    List<Invoice> getOrder(Long id);

    boolean saveAccount(Account account);

    boolean saveCart(Cart cart);

    Cart findRecentCart(Account account);

    boolean saveLineItems(List<LineItem> lineItems, int lineItemIndex);

    void deleteLineItem(Long lineNum, Long cartID);

    Optional<Cart> findCartById(long id);

    boolean validatePaymentInfo();

    ObjectNode viewInvoice(Long invoiceNum, ObjectMapper mapper);

    ObjectNode viewOrderHistory(String email, ObjectMapper mapper);
}
