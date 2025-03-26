package com.guhai.smartbuilding.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceControl {
    private Boolean warningLight;
    private Boolean fillLight;
    private Boolean exhaustFan;
    private Boolean alarm;
    private Boolean emergencyDoor;
} 