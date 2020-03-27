package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.LineItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Account services
 *
 * @author: matthewjflee
 */
public interface AccountServices {

    Optional<Account> findAccountByEmail(String email);

    void compareAccounts(Account account1, Account account2);

    boolean confirmAccount(Long id);

    List<Invoice> getOrder(Long id);

    boolean saveAccount(Account account, HttpSession session);

    boolean saveCart(Cart cart);

    void updateAccount(Account account, Account changed) throws InvalidInputException;

    Cart findRecentCart(Account account);

    boolean saveLineItems(List<LineItem> lineItems);

    void saveLineItem(LineItem li);

    void deleteLineItem(Long lineNum, Long cartID);

    Optional<Cart> findCartById(long id);

    boolean validatePaymentInfo();

    ObjectNode viewInvoice(Long invoiceNum, ObjectMapper mapper);

    ObjectNode viewOrderHistory(String email, ObjectMapper mapper);

    List<LineItem> getSavedCartLineItems(Cart cart);
}
