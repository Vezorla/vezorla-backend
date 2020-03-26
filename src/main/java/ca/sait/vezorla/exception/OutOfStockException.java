package ca.sait.vezorla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class OutOfStockException extends Exception {


    public OutOfStockException() {
        super("Out of Stock");
    }
}
