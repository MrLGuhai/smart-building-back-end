package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.entity.ThresholdRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ThresholdMapper {
    Thresholds getCurrentThresholds();
    int updateThresholds(Thresholds thresholds);
    

} 