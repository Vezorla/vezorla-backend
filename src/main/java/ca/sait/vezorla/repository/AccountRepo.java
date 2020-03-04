package ca.sait.vezorla.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ca.sait.vezorla.model.Account;
import org.springframework.stereotype.Repository;

/**
 * Handles Account table
 * @author: matthewjflee, jjrr1717
 */
@Repository
public interface AccountRepo extends JpaRepository<Account, String>{

	/**
	 * Confirm the account after creation. This is done to confirm the user's account after creation
	 * @param email Account email
	 * @return int number of Account got modify
	 */
	@Modifying //TODO MINH WHAT IS THIS?
	@Query("UPDATE Account a SET a.isConfirmed = true WHERE a.email = ?1")
	int confirm(String email);

	/**
	 * Find an account by the specified email and password
	 * @param email user email
	 * @param password user password
	 * @return <code>true</code> if account exists, <code>false</code> if account does not
	 */
	Optional<Account> findByEmailAndPassword(String email, String password);
}
