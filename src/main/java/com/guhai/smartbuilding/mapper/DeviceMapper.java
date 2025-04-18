package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    Device getDeviceStatus();
    
    int updateDeviceStatus(Device device);
    
    int insertControlRecord(DeviceControlRecord record);
    
    List<DeviceControlRecord> getControlRecords();
    
    List<DeviceControlRecord> getControlRecordsByType(@Param("deviceType") int deviceType);
    
    List<DeviceControlRecord> getDeviceControlRecords(@Param("deviceType") Integer deviceType);
    
    DeviceControlRecord getLatestDeviceControl(@Param("deviceType") Integer deviceType);
} 