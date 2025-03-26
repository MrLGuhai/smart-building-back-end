package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.Environment;
import com.guhai.smartbuilding.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/environment")
public class EnvironmentController {
    @Autowired
    private EnvironmentService environmentService;

    @GetMapping("/current")
    public ApiResponse getCurrentEnvironment() {
        Environment environment = environmentService.getCurrentEnvironment();
        return ApiResponse.success("获取成功", environment);
    }
} 