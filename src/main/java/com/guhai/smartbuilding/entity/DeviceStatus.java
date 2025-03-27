package com.guhai.smartbuilding.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatus {
    private Integer id;
    private Boolean warningLight;
    private Boolean fillLight;
    private Boolean exhaustFan;
    private Boolean alarm;
    private Boolean emergencyDoor;
    private Boolean dht11Status;
    private Boolean lightSensorStatus;
    private Date createTime;
} 