package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.mapper.UserMapper;
import com.guhai.smartbuilding.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean login(String username, String password) {
        return userMapper.login(username, password) != null;
    }

    @Override
    public boolean register(String username, String password) {
        if (userMapper.findByUsername(username) != null) {
            return false;
        }
        return userMapper.register(username, password) > 0;
    }
} 