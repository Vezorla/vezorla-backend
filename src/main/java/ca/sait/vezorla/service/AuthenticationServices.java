package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Authentication services
 *
 * @author: matthewjflee
 */
public interface AuthenticationServices {

    String generatePassword();

    void forgotPassword(String email);

    Optional<Account> login(String email, String password, HttpSession session);

    public void logout();
}
