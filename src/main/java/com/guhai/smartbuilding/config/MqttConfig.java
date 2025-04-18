package com.guhai.smartbuilding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {
    private String broker;
    private String clientId;
    private String username;
    private String password;
    private int connectionTimeout;
    private int keepAliveInterval;
    private boolean cleanSession;
    private boolean automaticReconnect;
    private int qos;
    
    // 主题配置
    private Topics topics;
    
    @Data
    public static class Topics {
        private String syncRequest;
        private String syncResponse;
        private String sensorData;
        private String deviceStatus;
        private String deviceControl;
        private String deviceControlResponse;
        private String thresholdSet;
        private String thresholdSetResponse;
    }
} 