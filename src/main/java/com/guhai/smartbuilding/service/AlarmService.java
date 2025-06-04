package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.AlarmRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmService {
    /**
     * 处理告警记录
     * @param alarmRecord 告警记录
     * @return 是否处理成功
     */
    boolean handleAlarmRecord(AlarmRecord alarmRecord);

    /**
     * 获取历史告警记录
     * @param limit 记录数量限制
     * @param alarmType 告警类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 告警记录列表
     */
    List<AlarmRecord> getAlarmRecords(Integer limit, Integer alarmType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取并清除未处理的告警记录
     * @return 未处理的告警记录列表
     */
    List<AlarmRecord> getAndClearUnprocessedAlarms();
} 