package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.LineItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * AccountServices interface.
 *
 * This interface outlines the services as it relates to
 * Accounts.
 *
 * This interface acts as the intermediary between the controllers
 * and the repositories.
 *
 * @author matthewjflee
 */
public interface AccountServices {

    /**
     * Finds an Account in the database given the email.
     *
     * @param email Account email.
     * @return Optional list containing either the Account
     * or null if the Account was not found.
     */
    Optional<Account> findAccountByEmail(String email);

    /**
     * Confirms the Account given the Account id.
     *
     * @param id Account ID.
     * @return Boolean True if confirmed, false otherwise.
     */
    boolean confirmAccount(Long id);

    /**
     * Saves the Account to the database given the Account.
     *
     * @param account Account to be saved.
     * @return Boolean True if saved, false otherwise.
     */
    boolean saveAccount(Account account);

    /**
     * Saves the Account to the database given the Account
     * and the user's session..
     *
     * @param account Account to be saved.
     * @param session User's session
     * @return Boolean True if saved, false otherwise.
     */
    boolean saveAccount(Account account, HttpSession session);

    /**
     * Saves the Cart to the database.
     *
     * @param cart Cart to be saved.
     * @return Boolean True if saved, false otherwise.
     */
    boolean saveCart(Cart cart);

    /**
     * Updates the Account in the database.
     *
     * @param account Account to be updated.
     * @param changed Changed Account to be used for the update.
     * @return Account that was updated.
     * @throws InvalidInputException If the input is invalid.
     */
    Account updateAccount(Account account, Account changed) throws InvalidInputException;

    /**
     * Finds the most recent Cart.
     *
     * @param account Account for Cart object.
     * @return Cart inside Account.
     */
    Cart findRecentCart(Account account);

    /**
     * Creates a new Cart for Account.
     *
     * @param account Account to create Cart for.
     * @return Created Cart.
     */
    Cart createNewCart(Account account);

    /**
     * Saves the LineItems list to the database.
     *
     * @param lineItems LineItems to be saved.
     * @return Boolean True if saved, false otherwise.
     */
    boolean saveLineItems(List<LineItem> lineItems);

    /**
     * Saves a LineItem to the database.
     *
     * @param li LineItems to be saved.
     */
    void saveLineItem(LineItem li);

    /**
     * Deletes a lineItem from the database.
     *
     * @param lineNum Line number to be deleted.
     * @param cartID Cart ID.
     */
    void deleteLineItem(Long lineNum, Long cartID);

    /**
     * Gets the Invoice to be viewed.
     *
     * @param invoiceNum Invoice number to get.
     * @param mapper Maps the Invoice to an Object.
     * @return ObjectNode that contains the Invoice.
     */
    ObjectNode viewInvoice(Long invoiceNum, ObjectMapper mapper);

    /**
     * Gets the past orders via a session.
     *
     * @param mapper Maps the Invoice to an Object.
     * @param session User's session.
     * @return ObjectNode that contains the past orders.
     */
    ObjectNode viewOrderHistory(ObjectMapper mapper, HttpSession session);
}
