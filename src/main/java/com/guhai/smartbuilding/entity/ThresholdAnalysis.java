package com.guhai.smartbuilding.entity;

import lombok.Data;
import java.util.Map;

@Data
public class ThresholdAnalysis {
    private int thresholdType;          // 阈值类型
    private int currentThreshold;       // 当前阈值
    private int suggestedThreshold;     // 建议阈值
    private double alarmFrequency;      // 告警频率（次/天）
    private double averageDeviation;    // 平均偏差
    private String suggestion;          // 调整建议
    private Map<String, Integer> timeDistribution;  // 时间分布（小时 -> 告警次数）
} 