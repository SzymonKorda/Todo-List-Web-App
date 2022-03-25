package com.szymon.todolist.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

}