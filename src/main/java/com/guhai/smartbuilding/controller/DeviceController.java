package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.DeviceControl;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.entity.DeviceStatus;
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
        DeviceStatus status = deviceService.getDeviceStatus();
        return ApiResponse.success("获取成功", status);
    }

    @GetMapping("/control")
    public ApiResponse getDeviceControl() {
        DeviceControl control = deviceService.getAllDevicesControlStatus();
        return ApiResponse.success("获取成功", control);
    }

    @PutMapping("/control")
    public ApiResponse updateDeviceControl(@RequestBody DeviceControl control) {
        if (deviceService.updateDeviceControl(control)) {
            return ApiResponse.success("更新成功", control);
        }
        return ApiResponse.error(500, "更新失败");
    }

    @GetMapping("/records")
    public ApiResponse getControlRecords() {
        List<DeviceControlRecord> records = deviceService.getControlRecords();
        return ApiResponse.success("获取成功", records);
    }

    @GetMapping("/records/type")
    public ApiResponse getControlRecordsByType(@RequestParam int deviceType) {
        List<DeviceControlRecord> records = deviceService.getControlRecordsByType(deviceType);
        return ApiResponse.success("获取成功", records);
    }
} 