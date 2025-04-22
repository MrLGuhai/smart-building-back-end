package com.guhai.smartbuilding.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmRecord {
    private Integer id;
    private Integer alarmType;        // 告警类型：1-温度过高，2-温度过低，3-湿度过高，4-湿度过低，5-光照过强，6-光照过弱
    private Double actualValue;       // 实际值
    private Double thresholdValue;    // 阈值
    private LocalDateTime createTime; // 创建时间
} 