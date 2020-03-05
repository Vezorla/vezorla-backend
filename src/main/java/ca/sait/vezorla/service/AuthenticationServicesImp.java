package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.AccountNotFoundException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.repository.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Service to interface with the AccountRepo regarding the authentication
 * @author: matthewjflee
 */
@Service
@AllArgsConstructor
public class AuthenticationServicesImp implements AuthenticationServices {

    private AccountRepo accountRepo;

    public void forgotPassword(String email) {

    }

    /**
     * Find an account with that email and password
     *
     * @author matthewjflee
     * @param email
     * @param password
     * @return
     */
//    public Optional<Account> login(String email, String password, HttpSession session) {
    public Account login(String email, String password, HttpSession session) {
//        Optional<Account> account = accountRepo.findByEmailAndPassword(email, password);
        Account account = accountRepo.findByEmailAndPassword(email, password);
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

    public void logout() {

    }

}
