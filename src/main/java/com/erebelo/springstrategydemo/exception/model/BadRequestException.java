package com.erebelo.springstrategydemo.exception.model;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message);
    }
}
