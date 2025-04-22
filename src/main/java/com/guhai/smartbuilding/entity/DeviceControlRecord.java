package com.guhai.smartbuilding.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceControlRecord {
    private Integer id;
    private Integer userId;
    private Integer deviceStatusId;
    private Integer deviceType;
    private Boolean controlAction;
    private LocalDateTime createTime;
} 