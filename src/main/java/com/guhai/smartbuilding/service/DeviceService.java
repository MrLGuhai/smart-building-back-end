package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.DeviceControlRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceService {
    // 获取当前设备状态
    Device getDeviceStatus();
    
    // 获取所有设备的控制状态
    Device getAllDevicesControlStatus();
    
    // 更新设备控制状态
    boolean updateDeviceControl(Device device);
    
    // 获取所有控制记录
    List<DeviceControlRecord> getControlRecords();
    
    // 根据设备类型获取控制记录
    List<DeviceControlRecord> getControlRecordsByType(int deviceType);
    
    // 获取设备控制记录
    List<DeviceControlRecord> getDeviceControlRecords(Integer deviceType);
    
    // 获取设备最新控制状态
    DeviceControlRecord getLatestDeviceControl(Integer deviceType);
    
    // 处理设备控制响应
    void handleDeviceControlResponse(Device device, boolean success);
    
    /**
     * 处理单片机自动控制设备状态
     * @param device 设备状态
     */
    void handleDeviceAutoControl(Device device);

    /**
     * 获取设备控制记录
     * @param limit 记录数量
     * @param deviceType 设备类型
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 控制记录列表
     */
    List<DeviceControlRecord> getControlRecords(
            Integer limit,
            Integer deviceType,
            Integer userId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
} 