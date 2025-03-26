package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DeviceMapper {
    DeviceStatus getDeviceStatus();
    
    // 获取设备控制记录
    List<DeviceControlRecord> getDeviceControlRecords(Integer deviceType);
    
    // 获取设备最新控制状态
    DeviceControlRecord getLatestDeviceControl(Integer deviceType);
    
    // 插入设备控制记录
    void insertDeviceControlRecord(DeviceControlRecord record);
} 