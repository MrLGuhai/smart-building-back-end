package com.guhai.smartbuilding.service;

import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.Thresholds;
import java.util.concurrent.CompletableFuture;

public interface MqttService {
    void connect();
    void disconnect();
    void publish(String topic, String message);
    void subscribe(String topic, int qos);
    void unsubscribe(String topic);
    String publishDeviceControl(Device device);
    String publishThresholdSet(Thresholds thresholds);
    
    // 添加回调相关方法
    void addCommandCallback(String commandId, CompletableFuture<Boolean> future);
    void removeCommandCallback(String commandId);
} 