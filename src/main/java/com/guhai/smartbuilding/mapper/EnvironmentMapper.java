package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.Environment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EnvironmentMapper {
    Environment getCurrentEnvironment();
    int insert(Environment environment);

    /**
     * 获取历史环境数据
     * @param limit 记录数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 环境数据记录列表
     */
    List<Environment> getHistoryRecords(
        @Param("limit") Integer limit,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
} 