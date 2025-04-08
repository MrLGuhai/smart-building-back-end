package com.guhai.smartbuilding.service;

public interface MqttService {
    void connect();
    void disconnect();
    void publish(String topic, String message);
    void subscribe(String topic, int qos);
    void unsubscribe(String topic);
} 