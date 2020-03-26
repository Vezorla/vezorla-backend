package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.UnableToSaveException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.AuthenticationServices;
import ca.sait.vezorla.service.AccountServices;
import ca.sait.vezorla.service.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(ClientRestController.URL)
public class ClientRestController {

    protected static final String URL = "/api/client/";

    private UserServices userServices;
    private AuthenticationServices authenticationServices;
    private AccountServices accountServices;

    @GetMapping("find/{id}")
    public void findById(@PathVariable Long id) {

    }

    @GetMapping("settings")
    public String getSettingsPage() {
        return null;
    }

    @PutMapping("account/update")
    public boolean updateAccount(@RequestBody Account account, HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean created = userServices.saveAccount(account);
        if (!created)
            throw new UnableToSaveException();
        else
            session.setAttribute("ACCOUNT", account);

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
     * @param id of the invoice to view.
     * @return the invoice to front-end
     */
    @GetMapping("invoice/{id}")
    public String viewInvoice(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper =new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountServices.viewInvoice(id, mapper));
    }

    /**
     * Method to view the order history from
     * a client's account
     * @param email of the account
     * @return the invoices to front-end
     */
    @GetMapping("order_history/{email}")
    public String viewOrderHistory(@PathVariable String email) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountServices.viewOrderHistory(email, mapper));
    }
}
