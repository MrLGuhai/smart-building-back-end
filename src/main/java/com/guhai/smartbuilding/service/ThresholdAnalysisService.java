package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.ThresholdAnalysis;
import java.util.List;

public interface ThresholdAnalysisService {
    /**
     * 分析阈值设置
     * @param days 分析天数
     * @param thresholdType 阈值类型（可选）
     * @return 分析结果列表
     */
    List<ThresholdAnalysis> analyzeThresholds(Integer days, Integer thresholdType);
} 