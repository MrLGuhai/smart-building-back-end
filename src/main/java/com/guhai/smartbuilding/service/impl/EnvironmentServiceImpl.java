package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.Environment;
import com.guhai.smartbuilding.mapper.EnvironmentMapper;
import com.guhai.smartbuilding.service.EnvironmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EnvironmentServiceImpl implements EnvironmentService {
    @Autowired
    private EnvironmentMapper environmentMapper;

    @Override
    public Environment getCurrentEnvironment() {
        return environmentMapper.getCurrentEnvironment();
    }
    
    @Override
    @Transactional
    public boolean updateEnvironment(Environment environment) {
        if (environment == null) {
            return false;
        }
        
        // 插入新的环境数据
        int result = environmentMapper.insert(environment);
        return result > 0;
    }

    @Override
    public List<Environment> getHistoryRecords(Integer limit, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            return environmentMapper.getHistoryRecords(limit, startTime, endTime);
        } catch (Exception e) {
            log.error("获取历史环境数据失败", e);
            return null;
        }
    }
} 