package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.DeviceControlRecord;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.enums.DeviceType;
import com.guhai.smartbuilding.mapper.DeviceMapper;
import com.guhai.smartbuilding.mapper.UserMapper;
import com.guhai.smartbuilding.service.DeviceService;
import com.guhai.smartbuilding.service.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MqttService mqttService;

    @Override
    public Device getDeviceStatus() {
        return deviceMapper.getDeviceStatus();
    }

    @Override
    public Device getAllDevicesControlStatus() {
        return deviceMapper.getDeviceStatus();
    }

    @Override
    @Transactional
    public boolean updateDeviceControl(Device device) {
        // 验证用户是否存在
        if (device.getUserId() == null) {
            return false;
        }
        User user = userMapper.selectById(device.getUserId());
        if (user == null) {
            return false;
        }
        
        // 获取当前设备状态
        Device currentStatus = deviceMapper.getDeviceStatus();
        if (currentStatus == null) {
            return false;
        }
        
        // 创建新的设备状态
        Device newStatus = new Device();
        newStatus.setWarningLight(currentStatus.getWarningLight());
        newStatus.setFillLight(currentStatus.getFillLight());
        newStatus.setExhaustFan(currentStatus.getExhaustFan());
        newStatus.setAlarm(currentStatus.getAlarm());
        newStatus.setEmergencyDoor(currentStatus.getEmergencyDoor());
        newStatus.setDht11Status(currentStatus.getDht11Status());
        newStatus.setLightSensorStatus(currentStatus.getLightSensorStatus());
        
        // 更新设备状态
        if (device.getWarningLight() != null) {
            newStatus.setWarningLight(device.getWarningLight());
        }
        if (device.getFillLight() != null) {
            newStatus.setFillLight(device.getFillLight());
        }
        if (device.getExhaustFan() != null) {
            newStatus.setExhaustFan(device.getExhaustFan());
        }
        if (device.getAlarm() != null) {
            newStatus.setAlarm(device.getAlarm());
        }
        if (device.getEmergencyDoor() != null) {
            newStatus.setEmergencyDoor(device.getEmergencyDoor());
        }
        
        // 创建回调Future
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        // 将控制命令转发到单片机
        String commandId = mqttService.publishDeviceControl(device);
        if (commandId == null) {
            return false;
        }
        
        // 添加回调
        mqttService.addCommandCallback(commandId, future);
        
        try {
            // 等待单片机响应，最多等待5秒
            Boolean success = future.get(5, TimeUnit.SECONDS);
            
            if (success) {
                // 如果控制成功，更新数据库中的设备状态
                deviceMapper.updateDeviceStatus(newStatus);
                
                // 记录控制操作
                if (device.getWarningLight() != null) {
                    DeviceControlRecord record = new DeviceControlRecord();
                    record.setUserId(device.getUserId());
                    record.setDeviceStatusId(newStatus.getId());
                    record.setDeviceType(DeviceType.WARNING_LIGHT.getCode());
                    record.setControlAction(device.getWarningLight());
                    deviceMapper.insertControlRecord(record);
                }
                
                if (device.getFillLight() != null) {
                    DeviceControlRecord record = new DeviceControlRecord();
                    record.setUserId(device.getUserId());
                    record.setDeviceStatusId(newStatus.getId());
                    record.setDeviceType(DeviceType.FILL_LIGHT.getCode());
                    record.setControlAction(device.getFillLight());
                    deviceMapper.insertControlRecord(record);
                }
                
                if (device.getExhaustFan() != null) {
                    DeviceControlRecord record = new DeviceControlRecord();
                    record.setUserId(device.getUserId());
                    record.setDeviceStatusId(newStatus.getId());
                    record.setDeviceType(DeviceType.EXHAUST_FAN.getCode());
                    record.setControlAction(device.getExhaustFan());
                    deviceMapper.insertControlRecord(record);
                }
                
                if (device.getAlarm() != null) {
                    DeviceControlRecord record = new DeviceControlRecord();
                    record.setUserId(device.getUserId());
                    record.setDeviceStatusId(newStatus.getId());
                    record.setDeviceType(DeviceType.ALARM.getCode());
                    record.setControlAction(device.getAlarm());
                    deviceMapper.insertControlRecord(record);
                }
                
                if (device.getEmergencyDoor() != null) {
                    DeviceControlRecord record = new DeviceControlRecord();
                    record.setUserId(device.getUserId());
                    record.setDeviceStatusId(newStatus.getId());
                    record.setDeviceType(DeviceType.EMERGENCY_DOOR.getCode());
                    record.setControlAction(device.getEmergencyDoor());
                    deviceMapper.insertControlRecord(record);
                }
            }
            
            return success;
        } catch (Exception e) {
            log.error("等待单片机响应超时或发生异常", e);
            return false;
        } finally {
            // 移除回调
            mqttService.removeCommandCallback(commandId);
        }
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
    
    @Override
    @Transactional
    public void handleDeviceControlResponse(Device device, boolean success) {
        if (success) {
            // 如果控制成功,更新数据库中的设备状态
            int result = deviceMapper.updateDeviceStatus(device);
            if (result <= 0) {
                log.error("设备控制成功但数据库更新失败: device={}", device);
            }
        } else {
            // 如果控制失败,记录错误日志
            log.error("设备控制失败: device={}", device);
        }
    }

    @Override
    @Transactional
    public void handleDeviceAutoControl(Device device) {
        // 获取当前设备状态
        Device currentStatus = deviceMapper.getDeviceStatus();
        if (currentStatus == null) {
            throw new RuntimeException("设备状态不存在");
        }

        // 更新设备状态
        deviceMapper.updateDeviceStatus(device);

        // 比较设备状态变化并记录
        if (currentStatus.getWarningLight() != device.getWarningLight()) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(1); // 系统自动控制
            record.setDeviceStatusId(currentStatus.getId());
            record.setDeviceType(DeviceType.WARNING_LIGHT.getCode());
            record.setControlAction(device.getWarningLight());
            deviceMapper.insertControlRecord(record);
        }

        if (currentStatus.getFillLight() != device.getFillLight()) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(1); // 系统自动控制
            record.setDeviceStatusId(currentStatus.getId());
            record.setDeviceType(DeviceType.FILL_LIGHT.getCode());
            record.setControlAction(device.getFillLight());
            deviceMapper.insertControlRecord(record);
        }

        if (currentStatus.getExhaustFan() != device.getExhaustFan()) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(1); // 系统自动控制
            record.setDeviceStatusId(currentStatus.getId());
            record.setDeviceType(DeviceType.EXHAUST_FAN.getCode());
            record.setControlAction(device.getExhaustFan());
            deviceMapper.insertControlRecord(record);
        }

        if (currentStatus.getAlarm() != device.getAlarm()) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(1); // 系统自动控制
            record.setDeviceStatusId(currentStatus.getId());
            record.setDeviceType(DeviceType.ALARM.getCode());
            record.setControlAction(device.getAlarm());
            deviceMapper.insertControlRecord(record);
        }

        if (currentStatus.getEmergencyDoor() != device.getEmergencyDoor()) {
            DeviceControlRecord record = new DeviceControlRecord();
            record.setUserId(1); // 系统自动控制
            record.setDeviceStatusId(currentStatus.getId());
            record.setDeviceType(DeviceType.EMERGENCY_DOOR.getCode());
            record.setControlAction(device.getEmergencyDoor());
            deviceMapper.insertControlRecord(record);
        }
    }
} 