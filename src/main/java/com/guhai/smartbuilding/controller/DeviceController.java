package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse getControlRecords() {
        List<DeviceControlRecord> records = deviceService.getControlRecords();
        return ApiResponse.success("获取成功", records);
    }

    @GetMapping("/control/records/{deviceType}")
    public ApiResponse getControlRecordsByType(@PathVariable Integer deviceType) {
        List<DeviceControlRecord> records = deviceService.getDeviceControlRecords(deviceType);
        return ApiResponse.success("获取成功", records);
    }
} 