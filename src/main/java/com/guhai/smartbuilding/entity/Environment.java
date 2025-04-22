package com.guhai.smartbuilding.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Environment {
    private Integer id;
    private Integer temperature;    // 温度
    private Integer humidity;       // 湿度
    private Integer light;          // 光照
    //private Integer co2;            // CO2浓度
    private Date createTime;        // 创建时间
} 