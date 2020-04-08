package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Account;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * AuthenticationServices interface.
 *
 * This interface outlines the services as it relates to
 * Authentication.
 *
 * This interface acts as the intermediary between the controllers
 * and the repositories.
 *
 * @author matthewjflee
 */
public interface AuthenticationServices {

    /**
     * Generates a pseudo-randomized password.
     *
     * @return String pseudo-randomized password.
     */
    String generatePassword();

    /**
     * Executes the forgot password use case.
     *
     * @param email Email to send reset password to.
     * @return boolean true if successful, false otherwise.
     * @throws InvalidInputException If forgot password inputs are
     * invalid.
     */
    boolean forgotPassword(String email) throws InvalidInputException;

    /**
     * Handles account logins.
     *
     * @param email Account email.
     * @param password Account password.
     * @param session Session for the login.
     * @return Optional list containing the Account if found, else
     * null inside the list.
     */
    Optional<Account> login(String email, String password, HttpSession session);
}
