package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.ThresholdRecord;
import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.entity.User;
import com.guhai.smartbuilding.mapper.ThresholdMapper;
import com.guhai.smartbuilding.mapper.ThresholdRecordMapper;
import com.guhai.smartbuilding.mapper.UserMapper;
import com.guhai.smartbuilding.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThresholdServiceImpl implements ThresholdService {
    @Autowired
    private ThresholdMapper thresholdMapper;
    
    @Autowired
    private ThresholdRecordMapper thresholdRecordMapper;
    
    @Autowired
    private UserMapper userMapper;

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
        
        // 插入新的阈值设置
        int result = thresholdMapper.updateThresholds(mergedThresholds);
        if (result <= 0) {
            return false;
        }
        
        // 记录阈值变化
        if (currentThresholds != null) {
            // 检查并记录温度阈值变化
            if (thresholds.getTemperature() != null && 
                !thresholds.getTemperature().equals(currentThresholds.getTemperature())) {
                ThresholdRecord record = new ThresholdRecord();
                record.setUserId(thresholds.getUserId());
                record.setThresholdId(mergedThresholds.getId());
                record.setThresholdType(1);
                record.setNewValue(thresholds.getTemperature());
                thresholdRecordMapper.insert(record);
            }
            
            // 检查并记录湿度阈值变化
            if (thresholds.getHumidity() != null && 
                !thresholds.getHumidity().equals(currentThresholds.getHumidity())) {
                ThresholdRecord record = new ThresholdRecord();
                record.setUserId(thresholds.getUserId());
                record.setThresholdId(mergedThresholds.getId());
                record.setThresholdType(2);
                record.setNewValue(thresholds.getHumidity());
                thresholdRecordMapper.insert(record);
            }
            
            // 检查并记录光照上限阈值变化
            if (thresholds.getLightUpper() != null && 
                !thresholds.getLightUpper().equals(currentThresholds.getLightUpper())) {
                ThresholdRecord record = new ThresholdRecord();
                record.setUserId(thresholds.getUserId());
                record.setThresholdId(mergedThresholds.getId());
                record.setThresholdType(3);
                record.setNewValue(thresholds.getLightUpper());
                thresholdRecordMapper.insert(record);
            }
            
            // 检查并记录光照下限阈值变化
            if (thresholds.getLightLower() != null && 
                !thresholds.getLightLower().equals(currentThresholds.getLightLower())) {
                ThresholdRecord record = new ThresholdRecord();
                record.setUserId(thresholds.getUserId());
                record.setThresholdId(mergedThresholds.getId());
                record.setThresholdType(4);
                record.setNewValue(thresholds.getLightLower());
                thresholdRecordMapper.insert(record);
            }
        }
        
        return true;
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
} 