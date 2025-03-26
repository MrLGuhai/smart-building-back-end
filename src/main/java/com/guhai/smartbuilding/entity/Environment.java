package com.guhai.smartbuilding.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Environment {
    private Double temperature;
    private Double humidity;
    private Double lightIntensity;
} 