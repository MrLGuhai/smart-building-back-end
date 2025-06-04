package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.AlarmRecord;
import com.guhai.smartbuilding.mapper.AlarmMapper;
import com.guhai.smartbuilding.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    private AlarmMapper alarmMapper;

    // 使用线程安全的集合存储未处理的告警记录
    private final ConcurrentLinkedQueue<AlarmRecord> unprocessedAlarms = new ConcurrentLinkedQueue<>();

    @Override
    @Transactional
    public boolean handleAlarmRecord(AlarmRecord alarmRecord) {
        try {
            // 保存到数据库
            int result = alarmMapper.insertAlarmRecord(alarmRecord);
            if (result > 0) {
                // 同时添加到未处理告警队列
                unprocessedAlarms.offer(alarmRecord);   // offer相比add，不会抛出异常，而是返回false
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<AlarmRecord> getAlarmRecords(Integer limit, Integer alarmType, LocalDateTime startTime, LocalDateTime endTime) {
        // 设置默认值
        if (limit == null) {
            limit = 100; // 默认获取100条记录
        }
        
        return alarmMapper.getAlarmRecords(limit, alarmType, startTime, endTime);
    }

    @Override
    public List<AlarmRecord> getAndClearUnprocessedAlarms() {
        List<AlarmRecord> result = new ArrayList<>();
        AlarmRecord record;
        while ((record = unprocessedAlarms.poll()) != null) {
            result.add(record);
        }
        return result;
    }
} 