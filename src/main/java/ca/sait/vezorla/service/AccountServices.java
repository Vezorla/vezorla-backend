package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServices {

    public boolean confirmAccount(Long id) {
        return false;
    }

    public Account findById(Long id) {
        return null;
    }

    public List<Invoice> getOrder(Long id) {
        return null;
    }

    // Note: This is already in AuthenticationServices!
    public Account login(String email, String password) {
        return null;
    }

    public boolean saveAccount(Account account) {
        return false;
    }

    public boolean validatePaymentInfo() {
        return false;
    }

}
