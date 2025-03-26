package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.DeviceControl;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import java.util.List;

public interface DeviceService {
    DeviceStatus getDeviceStatus();
    
    // 获取设备控制记录
    List<DeviceControlRecord> getDeviceControlRecords(Integer deviceType);
    
    // 获取设备最新控制状态
    DeviceControlRecord getLatestDeviceControl(Integer deviceType);
    
    // 控制设备
    void controlDevice(Integer userId, Integer deviceType, Boolean action);
    
    // 获取所有设备的当前控制状态
    DeviceControl getAllDevicesControlStatus();
} 