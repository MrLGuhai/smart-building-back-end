package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.Thresholds;

public interface ThresholdService {
    Thresholds getCurrentThresholds();
    boolean updateThresholds(Thresholds thresholds);
} 