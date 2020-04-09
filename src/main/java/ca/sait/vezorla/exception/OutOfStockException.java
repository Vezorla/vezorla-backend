package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * OutOfStockException class.
 * <p>
 * This class outlines the OutOfStockException and is thrown when
 * a product is out of stock.
 */
@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class OutOfStockException extends Exception {

    /**
     * Default constructor.
     * <p>
     * Sets the Exception message.
     */
    public OutOfStockException() {
        super("Out of Stock");
    }
}
