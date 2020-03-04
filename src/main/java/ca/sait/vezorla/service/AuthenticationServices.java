package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;

import java.util.Optional;

/**
 * Authentication services
 * @author: matthewjflee
 */
public interface AuthenticationServices {

	public void forgotPassword(String email);

	public Optional<Account> login(String email, String password);

	public void logout();
}
