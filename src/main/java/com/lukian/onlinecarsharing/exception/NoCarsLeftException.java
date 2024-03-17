package com.lukian.onlinecarsharing.exception;

public class NoCarsLeftException extends RuntimeException {
    public NoCarsLeftException(String message) {
        super(message);
    }
}
