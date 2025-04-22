package com.guhai.smartbuilding.entity;

import java.time.LocalDateTime;

public class DeviceControlRecord {
    private Integer id;
    private Integer userId;
    private Integer deviceStatusId;
    private Integer deviceType;
    private Boolean controlAction;
    private LocalDateTime createTime;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeviceStatusId() {
        return deviceStatusId;
    }

    public void setDeviceStatusId(Integer deviceStatusId) {
        this.deviceStatusId = deviceStatusId;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getControlAction() {
        return controlAction;
    }

    public void setControlAction(Boolean controlAction) {
        this.controlAction = controlAction;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
} 