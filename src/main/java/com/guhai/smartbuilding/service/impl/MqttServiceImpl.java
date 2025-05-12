package com.guhai.smartbuilding.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhai.smartbuilding.config.MqttConfig;
import com.guhai.smartbuilding.entity.Device;
import com.guhai.smartbuilding.entity.Environment;
import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.entity.AlarmRecord;
import com.guhai.smartbuilding.service.DeviceService;
import com.guhai.smartbuilding.service.EnvironmentService;
import com.guhai.smartbuilding.service.MqttService;
import com.guhai.smartbuilding.service.ThresholdService;
import com.guhai.smartbuilding.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDateTime;

@Slf4j
@Service
public class MqttServiceImpl implements MqttService {
    
    @Autowired
    private MqttConfig mqttConfig;
    
    @Autowired
    @Lazy
    private DeviceService deviceService;
    
    @Autowired
    @Lazy
    private ThresholdService thresholdService;
    
    @Autowired
    private EnvironmentService environmentService;
    
    @Autowired
    private ObjectMapper objectMapper;

    private MqttClient mqttClient;
    
    @Autowired
    private AlarmService alarmService;
    
    // 存储commandId和对应的回调
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> commandCallbacks = new ConcurrentHashMap<>();
    
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
                }
                
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    //log.info("收到消息: topic={}, message={}", topic, new String(message.getPayload()));
                    handleMessage(topic, new String(message.getPayload()));
                }
                
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.debug("消息发送完成");
                }
            });
            
            mqttClient.connect(options);
            log.info("MQTT连接成功");
            
            // 订阅所有需要的主题
            subscribe(mqttConfig.getTopics().getSyncRequest(), mqttConfig.getQos());
            subscribe(mqttConfig.getTopics().getSensorData(), mqttConfig.getQos());
            subscribe(mqttConfig.getTopics().getDeviceStatus(), mqttConfig.getQos());
            subscribe(mqttConfig.getTopics().getDeviceControlResponse(), mqttConfig.getQos());
            subscribe(mqttConfig.getTopics().getThresholdSetResponse(), mqttConfig.getQos());
            subscribe(mqttConfig.getTopics().getAlarmRecord(), mqttConfig.getQos());
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
        try {
            if (topic.equals(mqttConfig.getTopics().getSyncRequest())) {
                handleSyncRequest(message);
            } else if (topic.equals(mqttConfig.getTopics().getSensorData())) {
                handleSensorData(message);
            } else if (topic.equals(mqttConfig.getTopics().getDeviceStatus())) {
                handleDeviceStatus(message);
            } else if (topic.equals(mqttConfig.getTopics().getDeviceControlResponse())) {
                handleDeviceControlResponse(message);
            } else if (topic.equals(mqttConfig.getTopics().getThresholdSetResponse())) {
                handleThresholdSetResponse(message);
            } else if (topic.equals(mqttConfig.getTopics().getAlarmRecord())) {
                handleAlarmRecord(message);
            } else {
                log.warn("未知主题: {}", topic);
            }
        } catch (Exception e) {
            log.error("处理消息失败: topic={}, message={}", topic, message, e);
        }
    }

    private void handleSyncRequest(String message) {
        try {
            Map<String, String> request = objectMapper.readValue(message, Map.class);
            String type = request.get("type");

            Map<String, Object> response = new HashMap<>();
            response.put("type", type);
            response.put("timestamp", System.currentTimeMillis());

            if ("all".equals(type) || "threshold".equals(type)) {
                Thresholds thresholds = thresholdService.getCurrentThresholds();
                Map<String, Object> thresholdData = new HashMap<>();
                thresholdData.put("temperature", thresholds.getTemperature());
                thresholdData.put("humidity", thresholds.getHumidity());
                thresholdData.put("lightUpper", thresholds.getLightUpper());
                thresholdData.put("lightLower", thresholds.getLightLower());
                response.put("threshold", thresholdData);
            }

            if ("all".equals(type) || "device".equals(type)) {
                Device device = deviceService.getDeviceStatus();
                Map<String, Object> deviceData = new HashMap<>();
                deviceData.put("warningLight", device.getWarningLight());
                deviceData.put("fillLight", device.getFillLight());
                deviceData.put("exhaustFan", device.getExhaustFan());
                deviceData.put("alarm", device.getAlarm());
                deviceData.put("emergencyDoor", device.getEmergencyDoor());
                deviceData.put("dht11Status", device.getDht11Status());
                deviceData.put("lightSensorStatus", device.getLightSensorStatus());
                response.put("device", deviceData);
            }

            publish(mqttConfig.getTopics().getSyncResponse(), objectMapper.writeValueAsString(response));
            log.info("收到传感器数据同步请求数据: {}", request);
        } catch (Exception e) {
            log.error("处理同步请求失败", e);
        }
    }

    private void handleSensorData(String message) {
        try {
            Map<String, Object> data = objectMapper.readValue(message, Map.class);
            // 校验数据是否为空
            if (data == null || data.isEmpty()) {
                log.warn("收到空的传感器数据");
                return;
            }

            // 创建环境数据对象
            Environment environment = new Environment();
            environment.setTemperature((Integer) data.get("temperature"));
            environment.setHumidity((Integer) data.get("humidity"));
            environment.setLight((Integer) data.get("light"));
            environment.setSmoke((Integer) data.get("smoke"));

            // 更新环境数据
            environmentService.updateEnvironment(environment);
            //log.info("收到传感器数据: {}", data);
        } catch (Exception e) {
            log.error("处理传感器数据失败", e);
        }
    }

    private void handleDeviceStatus(String message) {
        try {
            Map<String, Object> data = objectMapper.readValue(message, Map.class);
            // 校验数据是否为空
            if (data == null || data.isEmpty()) {
                log.warn("收到空的设备状态数据");
                return;
            }

            // 创建外设状态对象
            Device device = new Device();
            device.setUserId(1);    // 单片机系统自动控制外设状态时，用户ID为1
            device.setWarningLight((Integer) data.get("warningLight") == 1);
            device.setFillLight((Integer) data.get("fillLight") == 1);
            device.setExhaustFan((Integer) data.get("exhaustFan") == 1);
            device.setAlarm((Integer) data.get("alarm") == 1);
            device.setEmergencyDoor((Integer) data.get("emergencyDoor") == 1);
            device.setDht11Status((Integer) data.get("dht11Status") == 1);
            device.setLightSensorStatus((Integer) data.get("lightSensorStatus") == 1);

            deviceService.handleDeviceAutoControl(device);
            log.info("收到设备状态数据并更新: {}", data);
        } catch (Exception e) {
            log.error("处理设备状态数据失败", e);
        }
    }
    
    private void handleDeviceControlResponse(String message) {
        try {
            Map<String, Object> response = objectMapper.readValue(message, Map.class);
            String commandId = (String) response.get("commandId");
            boolean success = (boolean) response.get("success");
            
            CompletableFuture<Boolean> future = commandCallbacks.get(commandId);
            if (future != null) {
                future.complete(success);
                commandCallbacks.remove(commandId);
                log.info("收到设备控制响应: {}", response);
            } else {
                log.warn("收到未知commandId的设备控制响应: {}", commandId);
            }
        } catch (Exception e) {
            log.error("处理设备控制响应失败", e);
        }
    }
    
    private void handleThresholdSetResponse(String message) {
        try {
            Map<String, Object> response = objectMapper.readValue(message, Map.class);
            String commandId = (String) response.get("commandId");
            boolean success = (boolean) response.get("success");
            
            CompletableFuture<Boolean> future = commandCallbacks.get(commandId);
            if (future != null) {
                future.complete(success);
                commandCallbacks.remove(commandId);
                log.info("收到阈值设置响应: {}", response);
            } else {
                log.warn("收到未知commandId的阈值设置响应: {}", commandId);
            }
        } catch (Exception e) {
            log.error("处理阈值设置响应失败", e);
        }
    }
    
    private void handleAlarmRecord(String payload) throws Exception {
        AlarmRecord alarmRecord = objectMapper.readValue(payload, AlarmRecord.class);
        alarmRecord.setCreateTime(LocalDateTime.now());
        alarmService.handleAlarmRecord(alarmRecord);
        log.info("收到报警记录: {}", alarmRecord);
    }
    
    @Override
    public String publishDeviceControl(Device device) {
        try {
            Map<String, Object> message = new HashMap<>();

            String commandId = UUID.randomUUID().toString();
            message.put("commandId", commandId);
            if(device.getWarningLight()!= null){
                message.put("warningLight", device.getWarningLight());
            } else if (device.getFillLight() != null) {
                message.put("fillLight", device.getFillLight());
            } else if (device.getExhaustFan()!= null) {
                message.put("exhaustFan", device.getExhaustFan());
            } else if (device.getAlarm() != null) {
                message.put("alarm", device.getAlarm());
            } else if (device.getEmergencyDoor() != null) {
                message.put("emergencyDoor", device.getEmergencyDoor());
            }
            message.put("timestamp", System.currentTimeMillis());
            
            publish(mqttConfig.getTopics().getDeviceControl(), objectMapper.writeValueAsString(message));
            return commandId;
        } catch (Exception e) {
            log.error("发布设备控制消息失败", e);
            return null;
        }
    }
    
    @Override
    public String publishThresholdSet(Thresholds thresholds) {
        try {
            Map<String, Object> message = new HashMap<>();

            String commandId = UUID.randomUUID().toString();
            message.put("commandId", commandId);
            if (thresholds.getTemperature() != null) {
                message.put("temperature", thresholds.getTemperature());
            }
            if (thresholds.getHumidity() != null) {
                message.put("humidity", thresholds.getHumidity());
            }
            if (thresholds.getLightUpper() != null) {
                message.put("lightUpper", thresholds.getLightUpper());
            }
            if (thresholds.getLightLower() != null) {
                message.put("lightLower", thresholds.getLightLower());
            }
            message.put("timestamp", System.currentTimeMillis());
            
            publish(mqttConfig.getTopics().getThresholdSet(), objectMapper.writeValueAsString(message));
            return commandId;
        } catch (Exception e) {
            log.error("发布阈值设置消息失败", e);
            return null;
        }
    }

    @Override
    public void addCommandCallback(String commandId, CompletableFuture<Boolean> future) {
        commandCallbacks.put(commandId, future);
    }

    @Override
    public void removeCommandCallback(String commandId) {
        commandCallbacks.remove(commandId);
    }
} 