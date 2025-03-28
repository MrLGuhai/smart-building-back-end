package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.ThresholdRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThresholdRecordMapper {
    // 插入阈值修改记录
    int insert(ThresholdRecord record);
    
    // 查询用户的阈值修改记录
    List<ThresholdRecord> selectByUserId(@Param("userId") int userId);
    
    // 查询用户的特定类型阈值修改记录
    List<ThresholdRecord> selectByUserIdAndType(@Param("userId") int userId, @Param("thresholdType") int thresholdType);
} 