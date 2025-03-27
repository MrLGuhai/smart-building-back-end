package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.service.AuthService;
import com.guhai.smartbuilding.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody User user) {
        if (authService.login(user.getUsername(), user.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            String token = JwtUtils.generateJwt(claims);
            user.setToken(token);
            return ApiResponse.success("登录成功", user);
        }
        return ApiResponse.error(401, "用户名或密码错误");
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody User user) {
        if (authService.register(user.getUsername(), user.getPassword())) {
            return ApiResponse.success("注册成功", user);
        }
        return ApiResponse.error(400, "用户名已存在");
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        return ApiResponse.success("退出成功", null);
    }
} 