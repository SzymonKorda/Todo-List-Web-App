package com.szymon.todolist.payload.request;

import lombok.*;

import java.util.Set;

import javax.validation.constraints.*;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private final String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private final String email;

    private final Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private final String password;

}