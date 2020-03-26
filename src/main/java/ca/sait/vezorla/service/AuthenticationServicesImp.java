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

@AllArgsConstructor
@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    private AccountRepo accountRepo;

    private UserServices userServices;

    private EmailServices emailServices;

    public String generatePassword() {
        return "Vezorla" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    public boolean forgotPassword(String email) throws InvalidInputException {

        boolean resetPassword = false;

        Optional<Account> accountOptional = userServices.findAccountByEmail(email);

        if(accountOptional.isPresent()) {

            String password = generatePassword();
            Account account = accountOptional.get();

            account.setPassword(password);

            accountRepo.save(account);
            emailServices.sendForgotPassword(email, password);

            resetPassword = true;

        }

        return resetPassword;

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
