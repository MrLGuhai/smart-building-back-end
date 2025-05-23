package com.guhai.smartbuilding.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.guhai.smartbuilding.entity.LoginResponse;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.mapper.UserMapper;
import com.guhai.smartbuilding.service.AuthService;
import com.guhai.smartbuilding.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginResponse login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            log.info("登录成功，用户信息: {}", user);
            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            String jwt = JwtUtils.generateJwt(claims);
            
            // 构建登录响应
            LoginResponse response = new LoginResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setToken(jwt);
            return response;
        }
        return null;
    }

    @Override
    public boolean register(String username, String password) {
        // 检查用户名是否已存在
        if (userMapper.selectByUsername(username) != null) {
            return false;
        }
        // 创建新用户
        User user = new User();
        user.setUsername(username);
        // 使用BCrypt加密密码
        user.setPassword(BCrypt.hashpw(password));
        return userMapper.insert(user) > 0;
    }
} 