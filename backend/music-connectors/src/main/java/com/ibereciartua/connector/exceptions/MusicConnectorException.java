package com.ibereciartua.connector.exceptions;

public class MusicConnectorException extends RuntimeException {
    public MusicConnectorException(String message) {
        super(message);
    }

    public MusicConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
