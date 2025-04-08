package com.guhai.smartbuilding.service.impl;

import com.guhai.smartbuilding.config.MqttConfig;
import com.guhai.smartbuilding.service.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Slf4j
@Service
public class MqttServiceImpl implements MqttService {
    
    @Autowired
    private MqttConfig mqttConfig;
    
    private MqttClient mqttClient;
    
    /**
     * PostConstruct注解：在Bean初始化完成后（构造函数执行完成后）立即执行被注解的方法。
     * 在这里用于在MQTT服务创建后自动连接到MQTT服务器。
     */
    @PostConstruct
    public void init() {
        connect();
    }
    
    /**
     * PreDestroy注解：在Bean销毁之前执行被注解的方法。
     * 在这里用于在MQTT服务销毁之前断开与MQTT服务器的连接。
     */
    @PreDestroy
    public void destroy() {
        disconnect();
    }
    
    @Override
    public void connect() {
        try {
            mqttClient = new MqttClient(mqttConfig.getBroker(), mqttConfig.getClientId(), new MemoryPersistence());
            
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttConfig.getUsername());
            options.setPassword(mqttConfig.getPassword().toCharArray());
            options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
            options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
            options.setCleanSession(mqttConfig.isCleanSession());
            options.setAutomaticReconnect(mqttConfig.isAutomaticReconnect());
            
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("MQTT连接丢失", cause);
                    // 可以在这里添加重连逻辑
                }
                
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    log.info("收到消息: topic={}, message={}", topic, new String(message.getPayload()));
                    // 在这里处理接收到的消息
                    handleMessage(topic, new String(message.getPayload()));
                }
                
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.debug("消息发送完成");
                }
            });
            
            mqttClient.connect(options);
            log.info("MQTT连接成功");
            
            // 订阅配置的主题
            if (mqttConfig.getTopics() != null) {
                for (String topic : mqttConfig.getTopics()) {
                    subscribe(topic, mqttConfig.getQos());
                }
            }
        } catch (MqttException e) {
            log.error("MQTT连接失败", e);
        }
    }
    
    @Override
    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                log.info("MQTT断开连接");
            }
        } catch (MqttException e) {
            log.error("MQTT断开连接失败", e);
        }
    }
    
    @Override
    public void publish(String topic, String message) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(mqttConfig.getQos());
                mqttClient.publish(topic, mqttMessage);
                log.info("发布消息: topic={}, message={}", topic, message);
            } else {
                log.warn("MQTT客户端未连接，无法发布消息");
            }
        } catch (MqttException e) {
            log.error("发布消息失败", e);
        }
    }
    
    @Override
    public void subscribe(String topic, int qos) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.subscribe(topic, qos);
                log.info("订阅主题: topic={}, qos={}", topic, qos);
            } else {
                log.warn("MQTT客户端未连接，无法订阅主题");
            }
        } catch (MqttException e) {
            log.error("订阅主题失败", e);
        }
    }
    
    @Override
    public void unsubscribe(String topic) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.unsubscribe(topic);
                log.info("取消订阅主题: topic={}", topic);
            } else {
                log.warn("MQTT客户端未连接，无法取消订阅主题");
            }
        } catch (MqttException e) {
            log.error("取消订阅主题失败", e);
        }
    }
    
    private void handleMessage(String topic, String message) {
        // 在这里实现消息处理逻辑
        // 例如：根据不同的topic处理不同类型的消息
        log.info("处理消息: topic={}, message={}", topic, message);
    }
} 