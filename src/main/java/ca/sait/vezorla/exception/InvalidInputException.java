package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InvalidInputException class.
 * <p>
 * This class outlines the InvalidInputException and is thrown when
 * invalid input for email and postal code is entered into the system.
 *
 * @author jjrr1717
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidInputException extends Exception {

    /**
     * Default constructor.
     * <p>
     * Sets the Exception message.
     */
    public InvalidInputException() {
        super("Invalid input");
    }
}
