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
    private String[] topics;
    private int qos = 1;
    private int connectionTimeout = 30;
    private int keepAliveInterval = 60;
    private boolean cleanSession = true;
    private boolean automaticReconnect = true;
} 