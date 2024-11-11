package com.ibereciartua.connector.exceptions;

public class PlaylistCreationException extends RuntimeException {
    public PlaylistCreationException(String message) {
        super(message);
    }

    public PlaylistCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
