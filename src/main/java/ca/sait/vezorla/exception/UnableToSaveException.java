package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to be thrown the application is unable to persist the request in the database
 * Returns 503
 *
 * @author: matthewjflee
 */
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class UnableToSaveException extends RuntimeException {
    public UnableToSaveException() {
        super("Unable to process request");
    }
}
