package com.guhai.smartbuilding.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ThresholdRecord {
    private int id;              // 记录ID
    private int userId;          // 用户ID
    private int thresholdId;     // 关联的阈值设置ID
    private int thresholdType;   // 阈值类型：1-温度 2-湿度 3-光照上限 4-光照下限
    private int newValue;        // 新的阈值
    private Date createTime;     // 创建时间
} 