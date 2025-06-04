package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.entity.AlarmRecord;
import com.guhai.smartbuilding.service.AlarmService;
import com.guhai.smartbuilding.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarm")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;
    
    /**
     * 获取历史告警记录
     * @param limit 记录数量限制
     * @param alarmType 告警类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 告警记录列表
     */
    @GetMapping("/records")
    public ApiResponse getAlarmRecords(
            @RequestParam(required = false, defaultValue = "100") Integer limit,
            @RequestParam(required = false) Integer alarmType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        try {
            // 获取告警记录
            List<AlarmRecord> records = alarmService.getAlarmRecords(
                limit,
                alarmType,
                startTime,
                endTime
            );
            
            // 返回成功响应
            return ApiResponse.success("获取成功", Map.of("records", records));
        } catch (Exception e) {
            // 返回错误响应
            return ApiResponse.error(500, "获取告警记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取未处理的告警记录
     * @return 未处理的告警记录列表
     */
    @GetMapping("/unprocessed")
    public ApiResponse getUnprocessedAlarms() {
        try {
            List<AlarmRecord> records = alarmService.getAndClearUnprocessedAlarms();
            return ApiResponse.success("获取成功", Map.of("records", records));
        } catch (Exception e) {
            return ApiResponse.error(500, "获取未处理告警记录失败：" + e.getMessage());
        }
    }
} 