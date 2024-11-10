package com.ibereciartua.app.config.exception;

/**
 * Exception to be thrown when an internal server error occurs.
 * This exception is handled by the {@link GlobalExceptionHandler} class.
 */
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }
}
