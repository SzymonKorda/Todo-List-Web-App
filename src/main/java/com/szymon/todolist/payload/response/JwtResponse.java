package com.szymon.todolist.payload.response;

import lombok.*;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    private final String accessToken;
    private final String type = "Bearer";
    private final Long id;
    private final String username;
    private final String email;
    private final List<String> roles;
}