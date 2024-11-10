package com.ibereciartua.app.config.exception;

/**
 * Class to represent an error message in the API.
 * This class is used to return a proper response when an exception is thrown.
 */
public class ApiError {

    private final String message;

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(final Exception e) {
        this.message = e.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
