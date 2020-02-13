package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;

public interface AuthenticationServices {

	   public void forgotPassword(String email);
	    public Account login(String email, String password);
	    public void logout();
}
