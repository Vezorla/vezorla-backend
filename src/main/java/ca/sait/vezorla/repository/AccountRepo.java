package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Accounts table in the Vezorla database
 */
public interface AccountRepo extends JpaRepository<Account, Long> {
    /**
     * Confirm the account after creation. This is done to confirm the user's account after creation
     * @param id Account ID
     * @return boolean of confirmation success
     */
    boolean confirm(Long id);

    /**
     * Find an account by the email
     * @param email user's email
     * @return Account
     */
    Account findByEmail(String email);

    /**
     * Find an account by the specified email and password
     * @param email user email
     * @param password user password
     * @return <code>true</code> if account exists, <code>false</code> if account does not
     */
    boolean findByEmailAndPassword(String email, String password);
}
