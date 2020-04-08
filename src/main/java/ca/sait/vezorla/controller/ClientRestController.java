package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.PasswordMismatchException;
import ca.sait.vezorla.exception.UnableToSaveException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.AccountServices;
import ca.sait.vezorla.service.AuthenticationServices;
import ca.sait.vezorla.service.EmailServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * ClientRestController Class.
 *
 * This class maps the frontend to the backend. It does
 * so by implementing Spring RestController annotation.
 *
 * Lombok is used to reduce boilerplate code for constructors.
 *
 * @author matthewjflee
 */
@AllArgsConstructor
@RestController
@RequestMapping(ClientRestController.URL)
public class ClientRestController {

    protected static final String URL = "/api/client/";

    private AuthenticationServices authenticationServices;
    private AccountServices accountServices;
    private EmailServices emailServices;

    /**
     * Create a new account.
     *
     * @param body: JSON sending email and password
     * @return boolean true if created, false otherwise
     * @throws InvalidInputException If input is invalid
     * @author matthewjflee
     */
    @PostMapping("create-account")
    public boolean createAccount(@RequestBody String body) throws InvalidInputException {
        String email = null;
        String password = null;
        String rePassword = null;

        try {
            Object obj = new JSONParser().parse(body);
            JSONObject jo = (JSONObject) obj;
            email = (String) jo.get("email");
            password = (String) jo.get("password");
            rePassword = (String) jo.get("rePassword");
        } catch (ParseException ignored) {
        }

        //Check if password and rePassword are the same
        assert password != null;
        if (!password.equals(rePassword))
            throw new PasswordMismatchException();

        //Check if account exists
        Optional<Account> newAccount = accountServices.findAccountByEmail(email);
        if (newAccount.isPresent() && newAccount.get().isUserCreated()) //Account exists.
            return false;
        else {
            newAccount = Optional.of(new Account(email, password));
            if (!accountServices.saveAccount(newAccount.get()))
                throw new UnableToSaveException();
            else {
                //Create cart and persist cart
                Cart cart = new Cart(newAccount.get());
                newAccount.get().getCarts().add(cart);
                accountServices.saveCart(cart);

                //Send email
                emailServices.sendCreateAccountEmail(email);
            }
        }

        return true;
    }

    /**
     * Update an existing account's information.
     *
     * @param sendAccount account changed in the front-end
     * @param request     user request
     * @return if it was updated
     * @throws InvalidInputException thrown if phone number or postal is invalid
     * @author matthewjflee
     */
    @PutMapping("account/update")
    public boolean updateAccount(@RequestBody Account sendAccount, HttpServletRequest request) throws InvalidInputException {
        Account account = null;
        boolean created;
        HttpSession session = request.getSession();
        Optional<Account> findAccount = accountServices.findAccountByEmail(sendAccount.getEmail());
        if (findAccount.isPresent())
            account = findAccount.get();

        Account updateAccount = accountServices.updateAccount(account, sendAccount);

        Account currentAccount = (Account) session.getAttribute("ACCOUNT");

        created = (currentAccount.getAccountType() == 'A') ? accountServices.saveAccount(updateAccount)
                : accountServices.saveAccount(updateAccount, session);

        if (!created)
            throw new UnableToSaveException();

        return created;
    }

    /**
     * Gets a specified order.
     *
     * Not implemented.
     *
     * @param id Order ID to get from the database.
     * @return List containing the specified Invoice Order.
     */
    @GetMapping("order/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

    /**
     * Method to view an invoice from a
     * client's account.
     *
     * @param id of the invoice to view.
     * @return the invoice to front-end
     * @throws JsonProcessingException If JSON cannot be processed
     */
    @GetMapping("invoice/{id}")
    public String viewInvoice(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountServices.viewInvoice(id, mapper));
    }

    /**
     * Method to view the order history from
     * a client's account.
     *
     * @param session User's session
     * @return the invoices to front-end
     * @throws JsonProcessingException If JSON cannot be processed
     * @author jjrr1717
     */
    @GetMapping("order_history")
    public String viewOrderHistory(HttpSession session) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountServices.viewOrderHistory(mapper, session));
    }
}