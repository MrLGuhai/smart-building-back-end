package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.ThresholdRecord;
import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.enums.ThresholdType;
import com.guhai.smartbuilding.mapper.ThresholdMapper;
import com.guhai.smartbuilding.mapper.ThresholdRecordMapper;
import com.guhai.smartbuilding.mapper.UserMapper;
import com.guhai.smartbuilding.service.ThresholdService;
import com.guhai.smartbuilding.service.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ThresholdServiceImpl implements ThresholdService {
    @Autowired
    private ThresholdMapper thresholdMapper;
    
    @Autowired
    private ThresholdRecordMapper thresholdRecordMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MqttService mqttService;

    @Override
    public Thresholds getCurrentThresholds() {
        return thresholdMapper.getCurrentThresholds();
    }

    @Override
    @Transactional
    public boolean updateThresholds(Thresholds thresholds) {
        // 检查用户是否存在
        User user = userMapper.selectById(thresholds.getUserId());
        if (user == null) {
            return false;
        }
        
        // 获取当前阈值
        Thresholds currentThresholds = thresholdMapper.getCurrentThresholds();
        
        // 合并阈值设置
        Thresholds mergedThresholds = new Thresholds();
        mergedThresholds.setUserId(thresholds.getUserId());
        mergedThresholds.setTemperature(thresholds.getTemperature() != null ? 
            thresholds.getTemperature() : currentThresholds.getTemperature());
        mergedThresholds.setHumidity(thresholds.getHumidity() != null ? 
            thresholds.getHumidity() : currentThresholds.getHumidity());
        mergedThresholds.setLightUpper(thresholds.getLightUpper() != null ? 
            thresholds.getLightUpper() : currentThresholds.getLightUpper());
        mergedThresholds.setLightLower(thresholds.getLightLower() != null ? 
            thresholds.getLightLower() : currentThresholds.getLightLower());
        
        // 创建回调Future
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        // 将阈值设置转发到单片机
        String commandId = mqttService.publishThresholdSet(mergedThresholds);
        if (commandId == null) {
            return false;
        }
        
        // 添加回调
        mqttService.addCommandCallback(commandId, future);
        
        try {
            // 等待单片机响应，最多等待5秒
            Boolean success = future.get(5, TimeUnit.SECONDS);
            
            if (success) {
                // 如果设置成功，更新数据库中的阈值
                thresholdMapper.updateThresholds(mergedThresholds);
                
                // 记录阈值变化
                if (currentThresholds != null) {
                    // 检查并记录温度阈值变化
                    if (thresholds.getTemperature() != null && 
                        !thresholds.getTemperature().equals(currentThresholds.getTemperature())) {
                        ThresholdRecord record = new ThresholdRecord();
                        record.setUserId(thresholds.getUserId());
                        record.setThresholdId(mergedThresholds.getId());
                        record.setThresholdType(ThresholdType.TEMPERATURE.getCode());
                        record.setOldValue(currentThresholds.getTemperature());
                        record.setNewValue(thresholds.getTemperature());
                        thresholdRecordMapper.insert(record);
                    }
                    
                    // 检查并记录湿度阈值变化
                    if (thresholds.getHumidity() != null && 
                        !thresholds.getHumidity().equals(currentThresholds.getHumidity())) {
                        ThresholdRecord record = new ThresholdRecord();
                        record.setUserId(thresholds.getUserId());
                        record.setThresholdId(mergedThresholds.getId());
                        record.setThresholdType(ThresholdType.HUMIDITY.getCode());
                        record.setOldValue(currentThresholds.getHumidity());
                        record.setNewValue(thresholds.getHumidity());
                        thresholdRecordMapper.insert(record);
                    }
                    
                    // 检查并记录光照上限阈值变化
                    if (thresholds.getLightUpper() != null && 
                        !thresholds.getLightUpper().equals(currentThresholds.getLightUpper())) {
                        ThresholdRecord record = new ThresholdRecord();
                        record.setUserId(thresholds.getUserId());
                        record.setThresholdId(mergedThresholds.getId());
                        record.setThresholdType(ThresholdType.LIGHT_UPPER.getCode());
                        record.setOldValue(currentThresholds.getLightUpper());
                        record.setNewValue(thresholds.getLightUpper());
                        thresholdRecordMapper.insert(record);
                    }
                    
                    // 检查并记录光照下限阈值变化
                    if (thresholds.getLightLower() != null && 
                        !thresholds.getLightLower().equals(currentThresholds.getLightLower())) {
                        ThresholdRecord record = new ThresholdRecord();
                        record.setUserId(thresholds.getUserId());
                        record.setThresholdId(mergedThresholds.getId());
                        record.setThresholdType(ThresholdType.LIGHT_LOWER.getCode());
                        record.setOldValue(currentThresholds.getLightLower());
                        record.setNewValue(thresholds.getLightLower());
                        thresholdRecordMapper.insert(record);
                    }
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
    public List<ThresholdRecord> getThresholdRecords(int userId) {
        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        return thresholdRecordMapper.selectByUserId(userId);
    }

    @Override
    public List<ThresholdRecord> getThresholdRecordsByType(int userId, int thresholdType) {
        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        return thresholdRecordMapper.selectByUserIdAndType(userId, thresholdType);
    }
    
    @Override
    @Transactional
    public void handleThresholdSetResponse(Thresholds thresholds, boolean success) {
        if (success) {
            // 如果设置成功,插入新的阈值设置
            int result = thresholdMapper.updateThresholds(thresholds);
            if (result <= 0) {
                log.error("阈值设置成功但数据库更新失败: thresholds={}", thresholds);
            }
        } else {
            // 如果设置失败,记录错误日志
            log.error("阈值设置失败: thresholds={}", thresholds);
        }
    }

    @Override
    public List<ThresholdRecord> getThresholdRecords(Integer limit, Integer thresholdType, Integer userId, LocalDateTime startTime, LocalDateTime endTime) {
        return thresholdRecordMapper.getThresholdRecords(limit, thresholdType, userId, startTime, endTime);
    }
} 