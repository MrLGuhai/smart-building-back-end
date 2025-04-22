package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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
            @Param("limit") Integer limit,
            @Param("deviceType") Integer deviceType,
            @Param("userId") Integer userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
} 