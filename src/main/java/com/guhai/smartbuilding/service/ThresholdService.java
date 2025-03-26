package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.Thresholds;

public interface ThresholdService {
    Thresholds getCurrentThresholds();
    void updateThresholds(Thresholds thresholds);
} 