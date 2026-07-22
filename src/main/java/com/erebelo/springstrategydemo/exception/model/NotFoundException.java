package com.erebelo.springstrategydemo.exception.model;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(message);
    }
}
