package com.lukian.onlinecarsharing.exception;

public class CarIsAlreadyReturnedException extends RuntimeException {
    public CarIsAlreadyReturnedException(String message) {
        super(message);
    }
}
