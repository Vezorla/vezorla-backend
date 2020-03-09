package ca.sait.vezorla.service;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServicesImp implements AccountServices{

    @Autowired
    private AccountRepo accountRepo;

    public boolean confirmAccount(Long id) {
        return false;
    }

    public Account findById(Long id) {
        return null;
    }

    /**
     * The get() method gets all the accounts in the database and
     * returns an account if it is found or null if it is not.
     *
     * @param email Email of the searched for account.
     * @return Account object that matches the email, null if not found.
     */
    @Override
    public Account get(String email) {

        Account account = null;

        Iterable<Account> accounts = accountRepo.findAll();

        for(Account a: accounts) {
            if(a.getEmail().equalsIgnoreCase(email)) {
                account = a;
            }
        }

        return account;

    }

    public List<Invoice> getOrder(Long id) {
        return null;
    }

    public boolean saveAccount(Account account) {
        return false;
    }

    public boolean validatePaymentInfo() {
        return false;
    }

}
