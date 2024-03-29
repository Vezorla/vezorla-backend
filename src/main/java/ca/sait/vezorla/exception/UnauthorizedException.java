package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * UnauthorizedException class.
 * <p>
 * This class outlines the UnauthorizedException and is
 * thrown when the user is not authorized.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {

    /**
     * Default constructor.
     * <p>
     * Sets the Exception message.
     */
    public UnauthorizedException() {
        super("Not authorized");
    }
}
