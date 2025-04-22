package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.entity.AlarmRecord;
import com.guhai.smartbuilding.mapper.AlarmMapper;
import com.guhai.smartbuilding.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    @Transactional
    public boolean handleAlarmRecord(AlarmRecord alarmRecord) {
        try {
            int result = alarmMapper.insertAlarmRecord(alarmRecord);
            return result > 0;
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
} 