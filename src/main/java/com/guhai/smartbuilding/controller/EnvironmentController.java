package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.entity.Environment;
import com.guhai.smartbuilding.service.EnvironmentService;
import com.guhai.smartbuilding.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/environment")
public class EnvironmentController {
    @Autowired
    private EnvironmentService environmentService;

    @GetMapping("/current")
    public ApiResponse getCurrentEnvironment() {
        Environment environment = environmentService.getCurrentEnvironment();
        if (environment == null) {
            return ApiResponse.error(500, "获取当前环境数据失败");
        }
        return ApiResponse.success("获取成功", environment);
    }

    @GetMapping("/history")
    public ApiResponse getHistoryRecords(
            @RequestParam(required = false, defaultValue = "200") Integer limit,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Environment> records = environmentService.getHistoryRecords(limit, startTime, endTime);
        if (records == null) {
            return ApiResponse.error(500, "获取历史环境数据失败");
        }
        return ApiResponse.success("获取成功", Map.of("records", records));
    }
} 