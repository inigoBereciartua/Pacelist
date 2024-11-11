package com.ibereciartua.domain.exceptions;

public class MusicConnectorException extends RuntimeException {
    public MusicConnectorException(String message) {
        super(message);
    }

    public MusicConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
