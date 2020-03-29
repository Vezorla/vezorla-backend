package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the password and re-password are not matching
 * This is done for create account
 * Returns 406 HttpStatus
 *
 * @author matthewjflee
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Password and re-password do not match!");
    }
}
