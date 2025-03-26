package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.Environment;
import com.guhai.smartbuilding.mapper.EnvironmentMapper;
import com.guhai.smartbuilding.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {
    @Autowired
    private EnvironmentMapper environmentMapper;

    @Override
    public Environment getCurrentEnvironment() {
        return environmentMapper.getCurrentEnvironment();
    }
} 