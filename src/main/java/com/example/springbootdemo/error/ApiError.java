package com.example.springbootdemo.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class ApiError {

    private final LocalDateTime timestamp;
    private final ErrorCodes errorCode;
    private final String message;

    public ApiError(LocalDateTime timestamp, ErrorCodes errorCode, String message) {
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.message = message;

    }


    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }



}
