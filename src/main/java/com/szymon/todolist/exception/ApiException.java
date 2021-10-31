package com.szymon.todolist.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private final ZonedDateTime timestamp;

    public ApiException(String message, ZonedDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
