package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.DeviceControl;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import java.util.List;

public interface DeviceService {
    // 获取当前设备状态
    DeviceStatus getDeviceStatus();
    
    // 更新设备状态
    boolean updateDeviceStatus(DeviceStatus deviceStatus);
    
    // 获取所有设备的控制状态
    DeviceControl getAllDevicesControlStatus();
    
    // 更新设备控制状态
    boolean updateDeviceControl(DeviceControl control);
    
    // 获取所有控制记录
    List<DeviceControlRecord> getControlRecords();
    
    // 根据设备类型获取控制记录
    List<DeviceControlRecord> getControlRecordsByType(int deviceType);
    
    // 获取设备控制记录
    List<DeviceControlRecord> getDeviceControlRecords(Integer deviceType);
    
    // 获取设备最新控制状态
    DeviceControlRecord getLatestDeviceControl(Integer deviceType);
} 