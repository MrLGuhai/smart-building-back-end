package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.ThresholdRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ThresholdRecordMapper {
    // 插入阈值修改记录
    int insert(ThresholdRecord record);
    
    // 查询用户的阈值修改记录
    List<ThresholdRecord> selectByUserId(@Param("userId") int userId);
    
    // 查询用户的特定类型阈值修改记录
    List<ThresholdRecord> selectByUserIdAndType(@Param("userId") int userId, @Param("thresholdType") int thresholdType);

    /**
     * 查询历史阈值修改记录
     * @param limit 记录数量限制
     * @param thresholdType 阈值类型
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 阈值修改记录列表
     */
    List<ThresholdRecord> getThresholdRecords(
            @Param("limit") Integer limit,
            @Param("thresholdType") Integer thresholdType,
            @Param("userId") Integer userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

}