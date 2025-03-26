package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.DeviceStatus;
import com.guhai.smartbuilding.entity.DeviceControl;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.mapper.DeviceMapper;
import com.guhai.smartbuilding.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public DeviceStatus getDeviceStatus() {
        return deviceMapper.getDeviceStatus();
    }

    @Override
    public List<DeviceControlRecord> getDeviceControlRecords(Integer deviceType) {
        return deviceMapper.getDeviceControlRecords(deviceType);
    }

    @Override
    public DeviceControlRecord getLatestDeviceControl(Integer deviceType) {
        return deviceMapper.getLatestDeviceControl(deviceType);
    }

    @Override
    public void controlDevice(Integer userId, Integer deviceType, Boolean action) {
        DeviceControlRecord record = new DeviceControlRecord();
        record.setUserId(userId);
        record.setDeviceType(deviceType);
        record.setControlAction(action);
        deviceMapper.insertDeviceControlRecord(record);
    }

    @Override
    public DeviceControl getAllDevicesControlStatus() {
        DeviceControl control = new DeviceControl();
        
        // 获取各个设备的最新控制状态
        DeviceControlRecord warningLight = getLatestDeviceControl(1);
        DeviceControlRecord fillLight = getLatestDeviceControl(2);
        DeviceControlRecord exhaustFan = getLatestDeviceControl(3);
        DeviceControlRecord alarm = getLatestDeviceControl(4);
        DeviceControlRecord emergencyDoor = getLatestDeviceControl(5);
        
        // 设置控制状态
        control.setWarningLight(warningLight != null ? warningLight.getControlAction() : false);
        control.setFillLight(fillLight != null ? fillLight.getControlAction() : false);
        control.setExhaustFan(exhaustFan != null ? exhaustFan.getControlAction() : false);
        control.setAlarm(alarm != null ? alarm.getControlAction() : false);
        control.setEmergencyDoor(emergencyDoor != null ? emergencyDoor.getControlAction() : false);
        
        return control;
    }
} 