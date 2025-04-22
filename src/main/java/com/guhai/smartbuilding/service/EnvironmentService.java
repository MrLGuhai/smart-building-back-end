package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.Environment;
import java.time.LocalDateTime;
import java.util.List;

public interface EnvironmentService {
    Environment getCurrentEnvironment();
    
    boolean updateEnvironment(Environment environment);
    
    /**
     * 获取历史环境数据
     * @param limit 记录数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 环境数据记录列表
     */
    List<Environment> getHistoryRecords(Integer limit, LocalDateTime startTime, LocalDateTime endTime);
} 