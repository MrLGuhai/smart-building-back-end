package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.mapper.ThresholdMapper;
import com.guhai.smartbuilding.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThresholdServiceImpl implements ThresholdService {
    @Autowired
    private ThresholdMapper thresholdMapper;

    @Override
    public Thresholds getCurrentThresholds() {
        return thresholdMapper.getCurrentThresholds();
    }

    @Override
    public void updateThresholds(Thresholds thresholds) {
        thresholdMapper.updateThresholds(thresholds);
    }
} 