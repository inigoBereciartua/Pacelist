package com.ibereciartua.app.config.exception;

/**
 * Exception to be thrown when a bad request is made.
 * This exception is handled by the {@link GlobalExceptionHandler} class.
 */
public class BadRequestErrorException extends RuntimeException {

    public BadRequestErrorException() {
        super();
    }

    public BadRequestErrorException(String message) {
        super(message);
    }

    public BadRequestErrorException(Throwable cause) {
        super(cause);
    }
}
