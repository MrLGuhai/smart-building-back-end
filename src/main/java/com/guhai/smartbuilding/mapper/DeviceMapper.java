package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    // 获取当前设备状态
    DeviceStatus getDeviceStatus();
    
    // 更新设备状态
    int updateDeviceStatus(DeviceStatus deviceStatus);
    
    // 插入控制记录
    int insertControlRecord(DeviceControlRecord record);
    
    // 获取所有控制记录
    List<DeviceControlRecord> getControlRecords();
    
    // 根据设备类型获取控制记录
    List<DeviceControlRecord> getControlRecordsByType(@Param("deviceType") int deviceType);
    
    // 获取设备控制记录
    List<DeviceControlRecord> getDeviceControlRecords(@Param("deviceType") Integer deviceType);
    
    // 获取最新设备控制记录
    DeviceControlRecord getLatestDeviceControl(@Param("deviceType") Integer deviceType);
} 