package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.UnableToSaveException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.AuthenticationServices;
import ca.sait.vezorla.service.UserServices;
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


    @GetMapping("account/forgotpassword")
    public boolean forgotPassword(@RequestBody String email) throws InvalidInputException {

        return authenticationServices.forgotPassword(email);

    }

    @GetMapping("order/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

}
