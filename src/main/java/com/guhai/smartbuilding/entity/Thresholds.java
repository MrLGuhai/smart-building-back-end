package com.guhai.smartbuilding.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Thresholds {
    private Integer id;
    private Integer userId;          // 设置用户ID
    private Integer temperature;     // 温度阈值
    private Integer humidity;        // 湿度阈值
    private Integer lightUpper;      // 光照上限
    private Integer lightLower;      // 光照下限
    private Date createTime;         // 创建时间
} 