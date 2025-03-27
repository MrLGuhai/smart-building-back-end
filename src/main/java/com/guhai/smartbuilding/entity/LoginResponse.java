package com.guhai.smartbuilding.entity;

import lombok.Data;

@Data
public class LoginResponse {
    private int id;              // 用户ID
    private String username;     // 用户名
    private String token;        // JWT令牌
} 