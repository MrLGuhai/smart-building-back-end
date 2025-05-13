package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.LoginResponse;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.service.AuthService;
import com.guhai.smartbuilding.util.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/public-key")
    public ApiResponse getPublicKey() {
        return ApiResponse.success("获取成功", RsaUtils.getPublicKey());
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody User user) {
        // 解密用户名和密码
        String username = RsaUtils.decrypt(user.getUsername());
        String password = RsaUtils.decrypt(user.getPassword());
        
        if (username == null || password == null) {
            return ApiResponse.error(400, "解密失败");
        }
        
        LoginResponse loginResponse = authService.login(username, password);
        if (loginResponse != null) {
            return ApiResponse.success("登录成功", loginResponse);
        }
        return ApiResponse.error(401, "用户名或密码错误");
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody User user) {
        // 解密用户名和密码
        String username = RsaUtils.decrypt(user.getUsername());
        String password = RsaUtils.decrypt(user.getPassword());
        
        if (username == null || password == null) {
            return ApiResponse.error(400, "解密失败");
        }
        
        if (authService.register(username, password)) {
            return ApiResponse.success("注册成功", null);
        }
        return ApiResponse.error(400, "用户名已存在");
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        return ApiResponse.success("退出成功", null);
    }
} 