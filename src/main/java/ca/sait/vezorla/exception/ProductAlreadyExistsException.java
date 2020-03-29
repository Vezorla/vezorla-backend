package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throw a 409 if the product already exists
 *
 * @author matthewjflee
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException() {
        super("Product already exists!");
    }
}
