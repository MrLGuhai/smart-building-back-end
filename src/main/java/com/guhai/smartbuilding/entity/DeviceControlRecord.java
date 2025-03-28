package com.guhai.smartbuilding.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceControlRecord {
    private Integer id;
    private Integer userId;
    private Integer deviceStatusId;
    private Integer deviceType;
    private Boolean controlAction;
    private Date createTime;
} 