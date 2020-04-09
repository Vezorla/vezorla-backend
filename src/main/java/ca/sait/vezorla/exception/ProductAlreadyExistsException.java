package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ProductAlreadyExistsException class.
 * <p>
 * This class outlines the ProductAlreadyExistsException and is
 * thrown if the product already exists (Error 409).
 *
 * @author matthewjflee
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ProductAlreadyExistsException extends RuntimeException {

    /**
     * Default constructor.
     * <p>
     * Sets the RuntimeException message.
     */
    public ProductAlreadyExistsException() {
        super("Product already exists!");
    }
}
