package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Authentication services
 * @author: matthewjflee
 */
public interface AuthenticationServices {

	void forgotPassword(String email);

//	Optional<Account> login(String email, String password, HttpSession session);
	Account login(String email, String password, HttpSession session);

	void logout();
}
