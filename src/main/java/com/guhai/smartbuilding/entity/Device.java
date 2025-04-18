package com.guhai.smartbuilding.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Device {
    private Integer id;
    private Integer userId;          // 操作用户ID,在控制设备时使用
    private Boolean warningLight;    // 警示灯状态
    private Boolean fillLight;       // 补光灯状态
    private Boolean exhaustFan;      // 排气扇状态
    private Boolean alarm;           // 警报器状态
    private Boolean emergencyDoor;   // 应急逃生门状态
    private Boolean dht11Status;     // DHT11传感器状态
    private Boolean lightSensorStatus; // 光照传感器状态
    private Date createTime;         // 创建时间
} 