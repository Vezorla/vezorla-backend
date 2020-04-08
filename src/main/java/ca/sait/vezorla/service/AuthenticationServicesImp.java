package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.AccountNotFoundException;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.repository.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Optional;

/**
 * AuthenticationServicesImp services
 *
 * This class implements the AuthenticationServices interface.
 *
 * This class acts as the intermediary between the controllers
 * and the repositories.
 *
 * @author matthewjflee
 */
@AllArgsConstructor
@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    private AccountRepo accountRepo;
    private AccountServices accountServices;
    private EmailServices emailServices;

    /**
     * Generate a password for the user.
     *
     * @return generated password
     * @author kwistech
     */
    public String generatePassword() {
        return "AceiteDeOlivaVezorla" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Executes the forgot password use case.
     *
     * @param email Email to send reset password to.
     * @return boolean true if successful, false otherwise.
     * @throws InvalidInputException If forgot password inputs are
     * invalid.
     */
    public boolean forgotPassword(String email) throws InvalidInputException {
        Optional<Account> accountOptional = accountServices.findAccountByEmail(email);

        if (accountOptional.isPresent()) {
            String password = generatePassword();
            Account account = accountOptional.get();
            account.setPassword(password);

            accountRepo.save(account);
            emailServices.sendForgotPassword(email, password);

            return true;
        }

        return false;
    }

    /**
     * Check if the user's account is present.
     *
     * If it is, persist in session
     * If not, throw AccountNotFoundException
     *
     * @param email    email login
     * @param password password
     * @param session  user's session
     * @return account
     * @author matthewjflee
     */
    public Optional<Account> login(String email, String password, HttpSession session) {
        Optional<Account> account = accountRepo.findByEmailAndPassword(email, password);
        if (account.isPresent()) {
            session.setAttribute("ACCOUNT", account.get());

            //Check if temp account cookie is in session and remove it
            session.removeAttribute("TEMP-ACCOUNT");
        } else
            throw new AccountNotFoundException();

        return account;
    }
}
