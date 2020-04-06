package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Invalid input for email and postal code
 *
 * @author jjrr1717
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidInputException extends Exception {

    public InvalidInputException() {
        super("Invalid input");
    }
}
