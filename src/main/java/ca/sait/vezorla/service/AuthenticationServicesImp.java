package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.AccountNotFoundException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.repository.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    private AccountRepo accountRepo;

    public void forgotPassword(String email) {

    }

    /**
     * Check if the user's account is present
     * If it is, perisist in session
     * If not, throw AccountNotFoundException
     *
     * @author: matthewjflee
     * @param email
     * @param password
     * @param session
     * @return
     */
    public Optional<Account> login(String email, String password, HttpSession session) {
        Optional<Account> account = accountRepo.findByEmailAndPassword(email, password);
        if (account.isPresent())
            session.setAttribute("ACCOUNT", account.get());
        else
            throw new AccountNotFoundException();

        return account;
    }

    public void logout() {

    }

}
