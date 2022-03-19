package com.szymon.todolist.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}