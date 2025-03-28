package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.DeviceControl;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.mapper.DeviceMapper;
import com.guhai.smartbuilding.mapper.UserMapper;
import com.guhai.smartbuilding.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public DeviceStatus getDeviceStatus() {
        return deviceMapper.getDeviceStatus();
    }

    @Override
    @Transactional
    public boolean updateDeviceStatus(DeviceStatus deviceStatus) {
        // 获取当前设备状态
        DeviceStatus currentStatus = deviceMapper.getDeviceStatus();
        
        // 合并设备状态
        DeviceStatus mergedStatus = new DeviceStatus();
        mergedStatus.setWarningLight(deviceStatus.getWarningLight() != null ? 
            deviceStatus.getWarningLight() : currentStatus.getWarningLight());
        mergedStatus.setFillLight(deviceStatus.getFillLight() != null ? 
            deviceStatus.getFillLight() : currentStatus.getFillLight());
        mergedStatus.setExhaustFan(deviceStatus.getExhaustFan() != null ? 
            deviceStatus.getExhaustFan() : currentStatus.getExhaustFan());
        mergedStatus.setAlarm(deviceStatus.getAlarm() != null ? 
            deviceStatus.getAlarm() : currentStatus.getAlarm());
        mergedStatus.setEmergencyDoor(deviceStatus.getEmergencyDoor() != null ? 
            deviceStatus.getEmergencyDoor() : currentStatus.getEmergencyDoor());
        mergedStatus.setDht11Status(deviceStatus.getDht11Status() != null ? 
            deviceStatus.getDht11Status() : currentStatus.getDht11Status());
        mergedStatus.setLightSensorStatus(deviceStatus.getLightSensorStatus() != null ? 
            deviceStatus.getLightSensorStatus() : currentStatus.getLightSensorStatus());
        
        // 插入新的设备状态
        int result = deviceMapper.updateDeviceStatus(mergedStatus);
        return result > 0;
    }

    @Override
    public DeviceControl getAllDevicesControlStatus() {
        DeviceControl control = new DeviceControl();
        DeviceStatus status = deviceMapper.getDeviceStatus();
        
        if (status != null) {
            control.setWarningLight(status.getWarningLight());
            control.setFillLight(status.getFillLight());
            control.setExhaustFan(status.getExhaustFan());
            control.setAlarm(status.getAlarm());
            control.setEmergencyDoor(status.getEmergencyDoor());
        }
        
        return control;
    }

    @Override
    @Transactional
    public boolean updateDeviceControl(DeviceControl control) {
        // 验证用户是否存在
        if (control.getUserId() == null) {
            return false;
        }
        User user = userMapper.selectById(control.getUserId());
        if (user == null) {
            return false;
        }
        
        // 获取当前设备状态
        DeviceStatus currentStatus = deviceMapper.getDeviceStatus();
        if (currentStatus == null) {
            return false;
        }
        
        // 创建新的设备状态
        DeviceStatus newStatus = new DeviceStatus();
        newStatus.setWarningLight(currentStatus.getWarningLight());
        newStatus.setFillLight(currentStatus.getFillLight());
        newStatus.setExhaustFan(currentStatus.getExhaustFan());
        newStatus.setAlarm(currentStatus.getAlarm());
        newStatus.setEmergencyDoor(currentStatus.getEmergencyDoor());
        newStatus.setDht11Status(currentStatus.getDht11Status());
        newStatus.setLightSensorStatus(currentStatus.getLightSensorStatus());
        
        // 更新设备状态
        if (control.getWarningLight() != null) {
            newStatus.setWarningLight(control.getWarningLight());
        }
        if (control.getFillLight() != null) {
            newStatus.setFillLight(control.getFillLight());
        }
        if (control.getExhaustFan() != null) {
            newStatus.setExhaustFan(control.getExhaustFan());
        }
        if (control.getAlarm() != null) {
            newStatus.setAlarm(control.getAlarm());
        }
        if (control.getEmergencyDoor() != null) {
            newStatus.setEmergencyDoor(control.getEmergencyDoor());
        }
        
        // 插入新的设备状态
        int result = deviceMapper.updateDeviceStatus(newStatus);
        if (result <= 0) {
            return false;
        }
        
        // 记录控制操作
        if (control.getWarningLight() != null) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(control.getUserId());
            record.setDeviceStatusId(newStatus.getId());
            record.setDeviceType(1);
            record.setControlAction(control.getWarningLight());
            deviceMapper.insertControlRecord(record);
        }
        
        if (control.getFillLight() != null) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(control.getUserId());
            record.setDeviceStatusId(newStatus.getId());
            record.setDeviceType(2);
            record.setControlAction(control.getFillLight());
            deviceMapper.insertControlRecord(record);
        }
        
        if (control.getExhaustFan() != null) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(control.getUserId());
            record.setDeviceStatusId(newStatus.getId());
            record.setDeviceType(3);
            record.setControlAction(control.getExhaustFan());
            deviceMapper.insertControlRecord(record);
        }
        
        if (control.getAlarm() != null) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(control.getUserId());
            record.setDeviceStatusId(newStatus.getId());
            record.setDeviceType(4);
            record.setControlAction(control.getAlarm());
            deviceMapper.insertControlRecord(record);
        }
        
        if (control.getEmergencyDoor() != null) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(control.getUserId());
            record.setDeviceStatusId(newStatus.getId());
            record.setDeviceType(5);
            record.setControlAction(control.getEmergencyDoor());
            deviceMapper.insertControlRecord(record);
        }
        
        return true;
    }

    @Override
    public List<DeviceControlRecord> getControlRecords() {
        return deviceMapper.getControlRecords();
    }

    @Override
    public List<DeviceControlRecord> getControlRecordsByType(int deviceType) {
        return deviceMapper.getControlRecordsByType(deviceType);
    }

    @Override
    public List<DeviceControlRecord> getDeviceControlRecords(Integer deviceType) {
        return deviceMapper.getDeviceControlRecords(deviceType);
    }

    @Override
    public DeviceControlRecord getLatestDeviceControl(Integer deviceType) {
        return deviceMapper.getLatestDeviceControl(deviceType);
    }
} 