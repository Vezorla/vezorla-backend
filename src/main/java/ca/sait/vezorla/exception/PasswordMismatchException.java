package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * PasswordMismatchException class.
 * <p>
 * This class outlines the PasswordMismatchException and is
 * thrown when the password and re-password are not matching.
 * <p>
 * This is done for create account.
 * <p>
 * Returns 406 HttpStatus.
 *
 * @author matthewjflee
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class PasswordMismatchException extends RuntimeException {

    /**
     * Default constructor.
     * <p>
     * Sets the RuntimeException message.
     */
    public PasswordMismatchException() {
        super("Password and re-password do not match!");
    }
}
