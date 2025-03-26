package com.guhai.smartbuilding.service;

public interface AuthService {
    boolean login(String username, String password);
    boolean register(String username, String password);
} 