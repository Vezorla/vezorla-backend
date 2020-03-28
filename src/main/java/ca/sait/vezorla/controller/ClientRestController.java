package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.PasswordMismatchException;
import ca.sait.vezorla.exception.UnableToSaveException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.AccountServices;
import ca.sait.vezorla.service.AuthenticationServices;
import ca.sait.vezorla.service.UserServices;
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

@AllArgsConstructor
@RestController
@RequestMapping(ClientRestController.URL)
public class ClientRestController {

    protected static final String URL = "/api/client/";

    private AuthenticationServices authenticationServices;
    private AccountServices accountServices;

    /**
     * Create a new account
     *
     * @param body: JSON sending email and password
     * @author: matthewjflee
     */
    @PostMapping("create-account")
    public boolean createAccount(@RequestBody String body, HttpServletRequest request) {
        String email = null;
        String password = null;
        String rePassword = null;
        HttpSession session = request.getSession();

        try {
            Object obj = new JSONParser().parse(body);
            JSONObject jo = (JSONObject) obj;
            email = (String) jo.get("email");
            password = (String) jo.get("password");
            rePassword = (String) jo.get("rePassword");
        } catch (ParseException e) {
        }

        //Check if password and rePassword are the same
        assert password != null;
        if (!password.equals(rePassword))
            throw new PasswordMismatchException();

        //Check if account exists
        Optional<Account> newAccount = accountServices.findAccountByEmail(email);
        if (newAccount.isPresent()) //Account exists.
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
            }
        }

        return true;
    }

    @PutMapping("account/update")
    public boolean updateAccount(@RequestBody Account account, HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean created = accountServices.saveAccount(account, session);
        if (!created)
            throw new UnableToSaveException();

        return created;
    }


    @PutMapping("account/forgot-password")
    public boolean forgotPassword(@RequestBody String email) throws InvalidInputException {
        email = email.replaceAll("\"", "");
        return authenticationServices.forgotPassword(email);
    }

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
     */
    @GetMapping("invoice/{id}")
    public String viewInvoice(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountServices.viewInvoice(id, mapper));
    }

    /**
     * Method to view the order history from
     * a client's account
     *
     * @return the invoices to front-end
     */
    @GetMapping("order_history")
    public String viewOrderHistory(HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountServices.viewOrderHistory(mapper, request));
    }
}