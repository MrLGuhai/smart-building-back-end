package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.ThresholdRecord;
import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/thresholds")
public class ThresholdController {
    @Autowired
    private ThresholdService thresholdService;

    @GetMapping("/current")
    public ApiResponse getCurrentThresholds() {
        Thresholds thresholds = thresholdService.getCurrentThresholds();
        return ApiResponse.success("获取成功", thresholds);
    }

    @PostMapping("/update")
    public ApiResponse updateThresholds(@RequestBody Thresholds thresholds) {
        if (thresholdService.updateThresholds(thresholds)) {
            return ApiResponse.success("更新成功", thresholds);
        }
        return ApiResponse.error(500, "更新失败");
    }

    @GetMapping("/records")
    public ApiResponse getThresholdRecords(
            @RequestParam(required = false, defaultValue = "100") Integer limit,
            @RequestParam(required = false) Integer thresholdType,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        try {
            List<ThresholdRecord> records = thresholdService.getThresholdRecords(
                limit,
                thresholdType,
                userId,
                startTime ,
                endTime
            );
            
            return ApiResponse.success("获取成功", Map.of("records", records));
        } catch (Exception e) {
            return ApiResponse.error(500, "获取阈值修改记录失败：" + e.getMessage());
        }
    }

    @GetMapping("/records/type")
    public ApiResponse getThresholdRecordsByType(@RequestParam int userId, @RequestParam int thresholdType) {
        List<ThresholdRecord> records = thresholdService.getThresholdRecordsByType(userId, thresholdType);
        return ApiResponse.success("获取成功", records);
    }
} 