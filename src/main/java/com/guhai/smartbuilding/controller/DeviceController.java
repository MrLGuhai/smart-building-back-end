package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.DeviceControl;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/devices")
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
        // 遍历所有设备状态，记录变更
        if (control.getWarningLight() != null) {
            deviceService.controlDevice(1, 1, control.getWarningLight());
        }
        if (control.getFillLight() != null) {
            deviceService.controlDevice(1, 2, control.getFillLight());
        }
        if (control.getExhaustFan() != null) {
            deviceService.controlDevice(1, 3, control.getExhaustFan());
        }
        if (control.getAlarm() != null) {
            deviceService.controlDevice(1, 4, control.getAlarm());
        }
        if (control.getEmergencyDoor() != null) {
            deviceService.controlDevice(1, 5, control.getEmergencyDoor());
        }
        return ApiResponse.success("更新成功", control);
    }

    @GetMapping("/control/records/{deviceType}")
    public ApiResponse getDeviceControlRecords(@PathVariable Integer deviceType) {
        List<DeviceControlRecord> records = deviceService.getDeviceControlRecords(deviceType);
        return ApiResponse.success("获取成功", records);
    }
} 