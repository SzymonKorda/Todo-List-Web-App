package com.szymon.todolist.service;

import com.szymon.todolist.payload.response.JwtResponse;
import com.szymon.todolist.payload.request.LoginRequest;
import com.szymon.todolist.payload.request.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    void registerUser(SignupRequest signupRequest);
}
