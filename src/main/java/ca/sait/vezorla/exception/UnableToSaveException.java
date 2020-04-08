package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * UnableToSaveException class.
 *
 * This class outlines the UnableToSaveException and is thrown
 * when the application is unable to persist a request in the database.
 *
 * Returns 503.
 *
 * @author matthewjflee
 */
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class UnableToSaveException extends RuntimeException {

    /**
     * Default constructor.
     *
     * Sets the RuntimeException message.
     */
    public UnableToSaveException() {
        super("Unable to process request");
    }
}
