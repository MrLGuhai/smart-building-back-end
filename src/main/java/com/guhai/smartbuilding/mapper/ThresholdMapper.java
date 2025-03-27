package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.Thresholds;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThresholdMapper {
    Thresholds getCurrentThresholds();
    int updateThresholds(Thresholds thresholds);
} 