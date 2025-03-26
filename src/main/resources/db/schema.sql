create database if not exists smart_building;
use smart_building;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 环境数据表
CREATE TABLE IF NOT EXISTS environment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    temperature INT NOT NULL,
    humidity INT NOT NULL,
    light_intensity INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 设备状态表
CREATE TABLE IF NOT EXISTS device_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    warning_light BOOLEAN DEFAULT FALSE,
    fill_light BOOLEAN DEFAULT FALSE,
    exhaust_fan BOOLEAN DEFAULT FALSE,
    alarm BOOLEAN DEFAULT FALSE,
    emergency_door BOOLEAN DEFAULT FALSE,
    dht11_status VARCHAR(20) DEFAULT 'normal',
    light_sensor_status VARCHAR(20) DEFAULT 'normal',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 设备控制记录表
CREATE TABLE IF NOT EXISTS device_control_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL COMMENT '操作用户ID',
    device_type INT NOT NULL COMMENT '设备类型：1-警示灯，2-补光灯，3-排气扇，4-警报器，5-应急逃生门',
    control_action BOOLEAN NOT NULL COMMENT '控制动作：true-开启，false-关闭',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 阈值设置表
CREATE TABLE IF NOT EXISTS thresholds (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL COMMENT '设置用户ID',
    temperature INT NOT NULL,
    humidity INT NOT NULL,
    light_upper INT NOT NULL,
    light_lower INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 告警记录表
CREATE TABLE IF NOT EXISTS alarm_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    alarm_type INT NOT NULL COMMENT '告警类型：1-温度过高，2-温度过低，3-湿度过高，4-湿度过低，5-光照过强，6-光照过弱',
    actual_value INT NOT NULL COMMENT '实际值',
    threshold_value INT NOT NULL COMMENT '阈值',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入默认阈值设置
INSERT INTO thresholds (user_id, temperature, humidity, light_upper, light_lower)
VALUES (1, 25, 60, 1000, 100)
ON DUPLICATE KEY UPDATE id = id; 