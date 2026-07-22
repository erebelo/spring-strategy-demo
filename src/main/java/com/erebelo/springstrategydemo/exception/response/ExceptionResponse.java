package com.erebelo.springstrategydemo.exception.response;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ExceptionResponse(HttpStatus status, String message, Instant timestamp) {
}
