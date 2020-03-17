package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;

import java.util.List;

public interface AccountServices {

	   public boolean confirmAccount(Long id);
	    public Account findById(Long id);
	    public Account findByEmail(String email);
	    public List<Invoice> getOrder(Long id);
	    public boolean saveAccount(Account account);
	    public boolean validatePaymentInfo();
}
