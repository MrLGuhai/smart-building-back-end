package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.mapper.ThresholdMapper;
import com.guhai.smartbuilding.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThresholdServiceImpl implements ThresholdService {
    @Autowired
    private ThresholdMapper thresholdMapper;

    @Override
    public Thresholds getCurrentThresholds() {
        return thresholdMapper.getCurrentThresholds();
    }

    @Override
    @Transactional
    public boolean updateThresholds(Thresholds newThresholds) {
        // 获取当前阈值设置
        Thresholds currentThresholds = thresholdMapper.getCurrentThresholds();
        if (currentThresholds == null) {
            // 如果没有当前设置，直接插入新设置
            return thresholdMapper.updateThresholds(newThresholds) > 0;
        }

        // 合并阈值设置
        Thresholds mergedThresholds = new Thresholds();
        mergedThresholds.setUserId(newThresholds.getUserId());
        mergedThresholds.setTemperature(newThresholds.getTemperature() != null ? newThresholds.getTemperature() : currentThresholds.getTemperature());
        mergedThresholds.setHumidity(newThresholds.getHumidity() != null ? newThresholds.getHumidity() : currentThresholds.getHumidity());
        mergedThresholds.setLightUpper(newThresholds.getLightUpper() != null ? newThresholds.getLightUpper() : currentThresholds.getLightUpper());
        mergedThresholds.setLightLower(newThresholds.getLightLower() != null ? newThresholds.getLightLower() : currentThresholds.getLightLower());

        // 插入合并后的阈值设置
        return thresholdMapper.updateThresholds(mergedThresholds) > 0;
    }
} 