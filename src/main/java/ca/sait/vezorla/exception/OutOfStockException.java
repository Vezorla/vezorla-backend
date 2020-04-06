package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a product is out of stock
 */
@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class OutOfStockException extends Exception {


    public OutOfStockException() {
        super("Out of Stock");
    }
}
