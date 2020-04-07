package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * AccountNotFoundException class.
 *
 * This class outlines the AccountNotFoundException and is
 * thrown when the specified account is not found in the Accounts table.
 *
 * Returns a 401 http status.
 *
 * @author matthewjflee
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccountNotFoundException extends RuntimeException {

    /**
     * Default constructor.
     *
     * Sets the RuntimeException message.
     */
    public AccountNotFoundException() {
        super("Account not found");
    }

}
