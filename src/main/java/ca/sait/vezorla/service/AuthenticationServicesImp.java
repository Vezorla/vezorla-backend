package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.repository.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Optional<Account> login(String email, String password) {
        Optional<Account> checkAccount = accountRepo.findByEmailAndPassword(email, password);
        return checkAccount;
    }

    public void logout() {

    }

}
