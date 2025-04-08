# MQTT主题和消息格式设计

## 订阅主题

### 1. 传感器数据主题 (sensor/data)
- 主题: `sensor/data`
- 发布者: 单片机
- 订阅者: 后端服务
- 发布频率: 每秒一次
- 消息格式:
```json
{
    "temperature": 25.5,    // 温度值，单位：℃
    "humidity": 60.5,       // 湿度值，单位：%
    "light": 800,           // 光照值，单位：lux
    "timestamp": "2024-03-20T10:30:00"  // 数据采集时间
}
```

### 2. 设备控制响应主题 (device/control/response)
- 主题: `device/control/response`
- 发布者: 单片机
- 订阅者: 后端服务
- 发布时机: 执行控制命令后
- 消息格式:
```json
{
    "commandId": "uuid",    // 控制命令的唯一标识
    "warningLight": true,   // 执行后的状态
    "fillLight": true,      // 补光灯状态
    "exhaustFan": true,     // 排气扇状态
    "alarm": false,         // 警报器状态
    "emergencyDoor": false, // 应急逃生门状态
    "success": true,        // 是否执行成功
    "message": "执行成功",   // 执行结果描述
    "timestamp": "2024-03-20T10:30:00"  // 响应时间
}
```

### 3. 阈值设置响应主题 (threshold/set/response)
- 主题: `threshold/set/response`
- 发布者: 单片机
- 订阅者: 后端服务
- 发布时机: 执行阈值设置命令后
- 消息格式:
```json
{
    "commandId": "uuid",    // 设置命令的唯一标识
    "temperature": 26.0,    // 设置后的温度阈值
    "humidity": 65.0,       // 设置后的湿度阈值
    "lightUpper": 1000,     // 设置后的光照上限
    "lightLower": 500,      // 设置后的光照下限
    "success": true,        // 是否设置成功
    "message": "设置成功",   // 设置结果描述
    "timestamp": "2024-03-20T10:30:00"  // 响应时间
}
```

## 发布主题

### 1. 设备控制主题 (device/control)
- 主题: `device/control`
- 发布者: 后端服务
- 订阅者: 单片机
- 发布时机: 用户手动控制设备时
- 消息格式:
```json
{
    "commandId": "uuid",    // 控制命令的唯一标识
    "warningLight": true,   // 控制警示灯
    "fillLight": true,      // 控制补光灯
    "exhaustFan": true,     // 控制排气扇
    "alarm": false,         // 控制警报器
    "emergencyDoor": false, // 控制应急逃生门
    "timestamp": "2024-03-20T10:30:00"  // 控制命令时间
}
```

### 2. 阈值设置主题 (threshold/set)
- 主题: `threshold/set`
- 发布者: 后端服务
- 订阅者: 单片机
- 发布时机: 用户修改阈值时
- 消息格式:
```json
{
    "commandId": "uuid",    // 设置命令的唯一标识
    "temperature": 26.0,    // 温度阈值
    "humidity": 65.0,       // 湿度阈值
    "lightUpper": 1000,     // 光照上限
    "lightLower": 500,      // 光照下限
    "timestamp": "2024-03-20T10:30:00"  // 设置时间
}
```

## 主题说明

1. **传感器数据主题 (sensor/data)**
   - 单片机每秒发布一次环境数据
   - 后端服务接收后更新数据库中的环境记录
   - 用于实时监控和阈值判断

2. **设备控制响应主题 (device/control/response)**
   - 单片机在执行控制命令后发布响应
   - 后端服务接收后更新数据库中的设备状态
   - 用于确认控制命令的执行结果

3. **阈值设置响应主题 (threshold/set/response)**
   - 单片机在执行阈值设置命令后发布响应
   - 后端服务接收后更新数据库中的阈值记录
   - 用于确认阈值设置的结果

4. **设备控制主题 (device/control)**
   - 后端服务在用户手动控制设备时发布
   - 单片机接收后执行相应的控制命令
   - 用于远程控制设备

5. **阈值设置主题 (threshold/set)**
   - 后端服务在用户修改阈值时发布
   - 单片机接收后更新本地阈值
   - 用于自动控制设备

## 注意事项

1. 所有消息都包含时间戳，用于数据同步和问题排查
2. 设备状态和控制命令使用布尔值表示开关状态
3. 传感器数据使用浮点数表示，保留一位小数
4. 光照值使用整数表示，单位为lux
5. 所有主题的QoS级别设置为1，确保消息可靠传输
6. 控制命令和阈值设置命令需要包含唯一标识(commandId)，用于追踪命令执行情况 