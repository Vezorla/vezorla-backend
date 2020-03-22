package ca.sait.vezorla.service;

import java.util.List;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AccountServices {

	boolean confirmAccount(Long id);
	Account findById(Long id);
	List<Invoice> getOrder(Long id);
	boolean saveAccount(Account account);
	boolean validatePaymentInfo();
	ObjectNode viewInvoice(Long invoiceNum, ObjectMapper mapper);
	ObjectNode viewOrderHistory(String email, ObjectMapper mapper);
}
