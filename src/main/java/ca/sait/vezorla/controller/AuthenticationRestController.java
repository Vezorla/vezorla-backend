package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.service.AuthenticationServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping(AuthenticationRestController.URL)
public class AuthenticationRestController {

    protected static final String URL = "/client/";
    private AuthenticationServices authServices;

    /**
     * Find an account with that email and password
     *
     * @author matthewjflee
     * @param email
     * @param password
     * @return
     */
    public Optional<Account> login(String email, String password, HttpSession session) {
        Optional<Account> account = accountRepo.findByEmailAndPassword(email, password);
//        System.out.println("acc " + account.get().getEmail());
//        if(account.isPresent()) {
//            session.setAttribute("ACCOUNT", account);
        if(account.getEmail() != null) {
            session.setAttribute("ACCOUNT", account);
        } else

//        }
//        else
            throw new AccountNotFoundException();

        return account;
    }

    @GetMapping("login")
    public String getLoginPage() {
        return null;
    }

    @GetMapping("logout")
    public void logout() {

    }

}
