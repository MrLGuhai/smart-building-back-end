package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.AlarmRecord;
import com.guhai.smartbuilding.entity.ThresholdAnalysis;
import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.enums.ThresholdType;
import com.guhai.smartbuilding.service.AlarmService;
import com.guhai.smartbuilding.service.ThresholdAnalysisService;
import com.guhai.smartbuilding.service.ThresholdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ThresholdAnalysisServiceImpl implements ThresholdAnalysisService {
    @Autowired
    private AlarmService alarmService;
    
    @Autowired
    private ThresholdService thresholdService;

    @Override
    public List<ThresholdAnalysis> analyzeThresholds(Integer days, Integer thresholdType) {
        // 设置默认分析天数为7天
        if (days == null) {
            days = 7;
        }
        
        // 获取当前阈值设置
        Thresholds currentThresholds = thresholdService.getCurrentThresholds();
        if (currentThresholds == null) {
            return Collections.emptyList();
        }
        
        // 计算开始时间
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        
        // 获取告警记录，获取最新100条记录
        List<AlarmRecord> alarmRecords = alarmService.getAlarmRecords(null, null, startTime, endTime);
        
        // 按阈值类型分组分析
        List<ThresholdAnalysis> results = new ArrayList<>();
        
        // 如果指定了阈值类型，只分析该类型
        if (thresholdType != null) {
            results.add(analyzeThresholdType(thresholdType, currentThresholds, alarmRecords, days));
        } else {
            // 分析所有阈值类型
            for (ThresholdType type : ThresholdType.values()) {
                results.add(analyzeThresholdType(type.getCode(), currentThresholds, alarmRecords, days));
            }
        }
        
        return results;
    }
    
    private ThresholdAnalysis analyzeThresholdType(int thresholdType, Thresholds currentThresholds, 
            List<AlarmRecord> alarmRecords, int days) {
        ThresholdAnalysis analysis = new ThresholdAnalysis();
        analysis.setThresholdType(thresholdType);
        
        // 设置当前阈值
        switch (thresholdType) {
            case 1: // 温度
                analysis.setCurrentThreshold(currentThresholds.getTemperature());
                break;
            case 2: // 湿度
                analysis.setCurrentThreshold(currentThresholds.getHumidity());
                break;
            case 3: // 光照上限
                analysis.setCurrentThreshold(currentThresholds.getLightUpper());
                break;
            case 4: // 光照下限
                analysis.setCurrentThreshold(currentThresholds.getLightLower());
                break;
        }
        
        // 过滤相关告警记录
        List<AlarmRecord> typeAlarms = alarmRecords.stream()
                .filter(record -> {
                    int alarmType = record.getAlarmType();
                    switch (thresholdType) {
                        case 1: // 温度 - 匹配告警类型1和2
                            return alarmType == 1 || alarmType == 2;
                        case 2: // 湿度 - 匹配告警类型3和4
                            return alarmType == 3 || alarmType == 4;
                        case 3: // 光照上限 - 匹配告警类型5
                            return alarmType == 5;
                        case 4: // 光照下限 - 匹配告警类型6
                            return alarmType == 6;
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
        
        // 计算告警频率，保留3位小数
        double alarmFrequency = new BigDecimal((double) typeAlarms.size() / days)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
        analysis.setAlarmFrequency(alarmFrequency);
        
        // 计算平均偏差，保留3位小数
        double averageDeviation = 0.0;
        if (!typeAlarms.isEmpty()) {
            averageDeviation = typeAlarms.stream()  // 将告警记录列表转换为流
                    .mapToDouble(record -> {  // 将每条告警记录映射为一个double值
                        // 计算实际值与阈值之间的绝对差值
                        // 例如：如果温度阈值是30度，实际温度是35度，则偏差为|35-30|=5
                        return Math.abs(record.getActualValue() - record.getThresholdValue());
                    })
                    .average()  // 计算所有偏差值的平均值
                    .getAsDouble();  // 获取double值
            
            // 保留3位小数
            averageDeviation = new BigDecimal(averageDeviation)
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        analysis.setAverageDeviation(averageDeviation);
        
        // 分析时间分布
        // 创建一个TreeMap用于存储每个小时的告警次数，TreeMap会自动按照key（时间）升序排序
        // key是时间（格式：HH:00），value是该时段的告警次数
        Map<String, Integer> timeDistribution = new TreeMap<>();
        
        // 创建时间格式化器，将时间格式化为"HH:00"格式
        // 例如：14:30会被格式化为"14:00"
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00");
        
        // 遍历所有告警记录，统计每个时段的告警次数
        typeAlarms.forEach(record -> {
            // 将告警时间格式化为小时格式
            String hour = record.getCreateTime().format(hourFormatter);
            
            // 使用merge方法更新Map中的值
            // 如果该时段已存在，则值加1；如果不存在，则初始化为1
            // 例如：
            // 第一次遇到"14:00"时，timeDistribution.put("14:00", 1)
            // 第二次遇到"14:00"时，timeDistribution.put("14:00", 2)
            timeDistribution.merge(hour, 1, Integer::sum);
        });
        
        // 将时间分布结果设置到分析对象中
        analysis.setTimeDistribution(timeDistribution);
        
        // 生成建议
        String suggestion = generateSuggestion(thresholdType, alarmFrequency, averageDeviation, timeDistribution);
        analysis.setSuggestion(suggestion);
        
        // 计算建议阈值
        int suggestedThreshold = calculateSuggestedThreshold(thresholdType, currentThresholds, 
                typeAlarms, alarmFrequency, averageDeviation);
        analysis.setSuggestedThreshold(suggestedThreshold);
        
        return analysis;
    }
    
    private String generateSuggestion(int thresholdType, double alarmFrequency, 
            double averageDeviation, Map<String, Integer> timeDistribution) {
        List<String> suggestions = new ArrayList<>();
        
        // 基于告警频率的建议
        if (alarmFrequency > 3) {
            suggestions.add("告警频率较高，建议调整阈值");
        } else if (alarmFrequency > 1) {
            suggestions.add("告警频率适中，可考虑微调阈值");
        }
        
        // 基于偏差的建议
        if (averageDeviation > 20) {
            suggestions.add("实际值与阈值偏差较大，建议重新评估阈值设置");
        }
        
        // 基于时间分布的建议
        if (!timeDistribution.isEmpty()) {  // 如果存在时间分布数据
            // 找出告警次数最多的值
            int maxCount = timeDistribution.values().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);
            
            // 找出所有告警次数等于最大值的时段
            List<String> peakHours = timeDistribution.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxCount)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            
            if (!peakHours.isEmpty()) {
                // 生成建议文本，说明告警最集中的时段
                // 例如：如果peakHours是["19:00", "22:00"]，则生成：
                // "告警主要集中在19:00、22:00时段，可考虑为这些时段设置特殊阈值"
                String hoursStr = String.join("、", peakHours);
                suggestions.add(String.format("告警主要集中在%s时段，可考虑为这些时段设置特殊阈值", hoursStr));
            }
        }
        
        // 将所有建议用分号连接
        return String.join(";", suggestions);
    }
    
    private int calculateSuggestedThreshold(int thresholdType, Thresholds currentThresholds,
            List<AlarmRecord> typeAlarms, double alarmFrequency, double averageDeviation) {
        int currentThreshold = 0;
        switch (thresholdType) {
            case 1: // 温度
                currentThreshold = currentThresholds.getTemperature();
                break;
            case 2: // 湿度
                currentThreshold = currentThresholds.getHumidity();
                break;
            case 3: // 光照上限
                currentThreshold = currentThresholds.getLightUpper();
                break;
            case 4: // 光照下限
                currentThreshold = currentThresholds.getLightLower();
                break;
        }
        
        // 如果告警频率过高，根据实际值的分布调整阈值
        if (alarmFrequency > 3) {
            double averageActualValue = typeAlarms.stream()
                    .mapToDouble(AlarmRecord::getActualValue)
                    .average()
                    .orElse(currentThreshold);
            
            // 根据告警类型判断是上限还是下限
            // 告警类型：1-温度过高，2-温度回归正常，3-湿度过高，4-湿度回归正常，5-光照过强，6-光照过弱
            boolean isUpperLimit = typeAlarms.stream()
                    .anyMatch(record -> {
                        int alarmType = record.getAlarmType();
                        switch (thresholdType) {
                            case 1: // 温度
                                return alarmType == 1; // 温度过高
                            case 2: // 湿度
                                return alarmType == 3; // 湿度过高
                            case 3: // 光照上限
                                return alarmType == 5; // 光照过强
                            case 4: // 光照下限
                                return alarmType == 6; // 光照过弱
                            default:
                                return false;
                        }
                    });
            
            // 根据是上限还是下限决定调整方向
            if (isUpperLimit) {
                return (int) (averageActualValue + 5); // 上限类型，增加5个单位
            } else {
                return (int) (averageActualValue - 5); // 下限类型，减少5个单位
            }
        }
        
        return currentThreshold;
    }
} 