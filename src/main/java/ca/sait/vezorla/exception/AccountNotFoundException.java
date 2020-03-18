package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the specified account is not found in the Accounts table
 * Returns a 401 http status
 *
 * @author: matthewjflee
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException() {
        super("Account not found");
    }
}
