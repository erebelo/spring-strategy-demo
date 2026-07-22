package com.erebelo.springstrategydemo.exception.model;

public abstract class ApiException extends RuntimeException {

    protected ApiException(String message) {
        super(message);
    }
}
