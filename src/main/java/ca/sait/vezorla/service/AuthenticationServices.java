package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
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

    boolean forgotPassword(String email) throws InvalidInputException;

    Optional<Account> login(String email, String password, HttpSession session);
}
