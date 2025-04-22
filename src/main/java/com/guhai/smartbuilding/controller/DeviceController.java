package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/status")
    public ApiResponse getDeviceStatus() {
        Device status = deviceService.getDeviceStatus();
        return ApiResponse.success("获取成功", status);
    }

    @PutMapping("/control")
    public ApiResponse updateDeviceControl(@RequestBody Device device) {
        boolean success = deviceService.updateDeviceControl(device);
        if (success) {
            return ApiResponse.success("设备控制成功", device);
        } else {
            return ApiResponse.error(500, "设备控制失败");
        }
    }

    @GetMapping("/control/records")
    public ApiResponse getControlRecords(
            @RequestParam(required = false, defaultValue = "100") Integer limit,
            @RequestParam(required = false) Integer deviceType,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        try {
            List<DeviceControlRecord> records = deviceService.getControlRecords(
                    limit,
                    deviceType,
                    userId,
                    startTime,
                    endTime
            );
            return ApiResponse.success("获取成功", records);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取控制记录失败");
        }
    }

    @GetMapping("/control/records/{deviceType}")
    public ApiResponse getControlRecordsByType(@PathVariable Integer deviceType) {
        List<DeviceControlRecord> records = deviceService.getDeviceControlRecords(deviceType);
        return ApiResponse.success("获取成功", records);
    }
} 