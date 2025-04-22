package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.AlarmRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AlarmMapper {
    /**
     * 插入告警记录
     * @param alarmRecord 告警记录
     * @return 影响的行数
     */
    int insertAlarmRecord(@Param("alarmRecord") AlarmRecord alarmRecord);

    /**
     * 查询历史告警记录
     * @param limit 记录数量限制
     * @param alarmType 告警类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 告警记录列表
     */
    List<AlarmRecord> getAlarmRecords(
            @Param("limit") Integer limit,
            @Param("alarmType") Integer alarmType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
} 