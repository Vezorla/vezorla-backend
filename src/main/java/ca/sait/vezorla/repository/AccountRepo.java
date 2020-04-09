package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AccountRepo interface.
 * <p>
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 * <p>
 * Repository to interact with the Accounts table.
 */
@Repository
public interface AccountRepo extends JpaRepository<Account, String> {

    /**
     * Confirm the account after creation.
     * <p>
     * This is done to confirm the user's account after creation.
     *
     * @param email Account email
     * @return int number of Account got modify
     */
    @Modifying
    @Query("UPDATE Account a SET a.isConfirmed = true WHERE a.email = ?1")
    int confirm(String email);

    /**
     * Find an account by the specified email and password.
     *
     * @param email    user email
     * @param password user password
     * @return <code>true</code> if account exists, <code>false</code> if account does not
     */
    Optional<Account> findByEmailAndPassword(String email, String password);

    /**
     * Find all the user created accounts.
     *
     * @return list of all accounts
     * @author jjrr1717
     */
    @Query(value = "FROM Account a WHERE a.userCreated = true")
    List<Account> findAllUserCreatedAccounts();
}
