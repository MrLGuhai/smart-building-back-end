create database if not exists smart_building;
use smart_building;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 环境数据表
CREATE TABLE IF NOT EXISTS environment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    temperature INT NOT NULL,
    humidity INT NOT NULL,
    light INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 设备状态表
CREATE TABLE IF NOT EXISTS device_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    warning_light BOOLEAN NOT NULL ,
    fill_light BOOLEAN NOT NULL ,
    exhaust_fan BOOLEAN NOT NULL ,
    alarm BOOLEAN NOT NULL ,
    emergency_door BOOLEAN NOT NULL ,
    dht11_status BOOLEAN NOT NULL ,
    light_sensor_status BOOLEAN NOT NULL ,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 设备控制记录表
CREATE TABLE IF NOT EXISTS device_control_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL COMMENT '操作用户ID',
    device_status_id INT NOT NULL COMMENT '关联的设备状态ID',
    device_type INT NOT NULL COMMENT '设备类型：1-警示灯，2-补光灯，3-排气扇，4-警报器，5-应急逃生门',
    control_action BOOLEAN NOT NULL COMMENT '控制动作：true-开启，false-关闭',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 阈值设置表
CREATE TABLE IF NOT EXISTS thresholds (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    temperature INT NOT NULL,
    humidity INT NOT NULL,
    light_upper INT NOT NULL,
    light_lower INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 阈值修改记录表
CREATE TABLE IF NOT EXISTS threshold_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL COMMENT '操作用户ID',
    threshold_id INT NOT NULL COMMENT '关联的阈值设置ID',
    threshold_type INT NOT NULL COMMENT '1:温度 2:湿度 3:光照上限 4:光照下限',
    old_value INT NOT NULL COMMENT '原始阈值',
    new_value INT NOT NULL COMMENT '新的阈值',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 告警记录表
CREATE TABLE IF NOT EXISTS alarm_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    alarm_type INT NOT NULL COMMENT '告警类型：1-温度过高，2-温度回归正常，3-湿度过高，4-湿度回归正常，5-光照过强，6-光照过弱',
    actual_value INT NOT NULL COMMENT '实际值',
    threshold_value INT NOT NULL COMMENT '阈值',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入默认阈值设置
INSERT INTO thresholds (user_id, temperature, humidity, light_upper, light_lower)
VALUES (1, 25, 60, 1000, 100)
ON DUPLICATE KEY UPDATE id = id; 