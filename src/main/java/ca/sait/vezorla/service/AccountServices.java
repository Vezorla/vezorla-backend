package ca.sait.vezorla.service;

import java.util.List;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AccountServices {

	   	public boolean confirmAccount(Long id);
	    public Account findById(Long id);
	    public List<Invoice> getOrder(Long id);
	    public boolean saveAccount(Account account);
	    public boolean validatePaymentInfo();
	public ObjectNode viewInvoice(Long invoiceNum);
}
