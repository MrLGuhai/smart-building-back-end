package com.guhai.smartbuilding.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnvironmentRecord {
    private Integer id;
    private Double temperature;
    private Integer humidity;
    private Integer light;
    private LocalDateTime createTime;
} 