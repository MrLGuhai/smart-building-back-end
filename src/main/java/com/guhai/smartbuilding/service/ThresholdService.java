package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.ThresholdRecord;
import com.guhai.smartbuilding.entity.Thresholds;

import java.util.List;

public interface ThresholdService {
    // 获取当前阈值设置
    Thresholds getCurrentThresholds();
    
    // 更新阈值设置
    boolean updateThresholds(Thresholds thresholds);
    
    // 获取用户的阈值修改记录
    List<ThresholdRecord> getThresholdRecords(int userId);
    
    // 获取用户的特定类型阈值修改记录
    List<ThresholdRecord> getThresholdRecordsByType(int userId, int thresholdType);
    
    // 处理阈值设置响应
    void handleThresholdSetResponse(Thresholds thresholds, boolean success);
} 