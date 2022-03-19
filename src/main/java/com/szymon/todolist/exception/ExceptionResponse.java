package com.szymon.todolist.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ExceptionResponse {
    private final String message;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private final ZonedDateTime timestamp;
}
