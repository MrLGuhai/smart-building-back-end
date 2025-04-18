package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.Environment;

public interface EnvironmentService {
    Environment getCurrentEnvironment();
    
    boolean updateEnvironment(Environment environment);
} 