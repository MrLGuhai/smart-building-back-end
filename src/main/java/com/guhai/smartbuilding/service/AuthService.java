package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.LoginResponse;

public interface AuthService {
    LoginResponse login(String username, String password);
    boolean register(String username, String password);
} 