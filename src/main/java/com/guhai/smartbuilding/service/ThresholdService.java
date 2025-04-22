package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.ThresholdRecord;
import com.guhai.smartbuilding.entity.Thresholds;

import java.time.LocalDateTime;
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
    
    /**
     * 获取历史阈值修改记录
     * @param limit 记录数量限制
     * @param thresholdType 阈值类型
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 阈值修改记录列表
     */
    List<ThresholdRecord> getThresholdRecords(Integer limit, Integer thresholdType, Integer userId, LocalDateTime startTime, LocalDateTime endTime);
} 