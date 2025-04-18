# 智慧建筑系统API文档

## 通用说明

### 基础URL
```
http://localhost:8080
```

### 通用响应格式
```json
{
    "code": 200,          // 状态码：200成功，其他表示错误
    "message": "成功",    // 响应消息
    "data": {}           // 响应数据
}
```

### 错误码说明
- 200: 成功
- 400: 请求参数错误
- 401: 未授权/未登录
- 403: 权限不足
- 500: 服务器内部错误

## 认证相关接口

### 1. 用户登录
- **接口URL**: `/auth/login`
- **请求方式**: POST
- **请求参数**:
```json
{
    "username": "string",  // 用户名
    "password": "string"   // 密码
}
```
- **成功响应**:
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "id": 1,
        "username": "string",
        "token": "string"
    }
}
```
- **失败响应**:
```json
{
    "code": 401,
    "message": "用户名或密码错误",
    "data": null
}
```

### 2. 用户注册
- **接口URL**: `/auth/register`
- **请求方式**: POST
- **请求参数**:
```json
{
    "username": "string",  // 用户名
    "password": "string"   // 密码
}
```
- **成功响应**:
```json
{
    "code": 200,
    "message": "注册成功",
    "data": {
        "id": 1,
        "username": "string"
    }
}
```
- **失败响应**:
```json
{
    "code": 400,
    "message": "用户名已存在",
    "data": null
}
```

### 3. 用户登出
- **接口URL**: `/auth/logout`
- **请求方式**: POST
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "退出成功",
    "data": null
}
```

## 环境数据接口

### 1. 获取当前环境数据
- **接口URL**: `/environment/current`
- **请求方式**: GET
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "id": 1,
        "temperature": 25.5,
        "humidity": 60,
        "light": 800,
        "co2": 400,
        "createTime": "2024-03-21T10:00:00"
    }
}
```

## 设备控制接口

### 1. 获取设备状态
- **接口URL**: `/device/status`
- **请求方式**: GET
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "id": 1,
        "warningLight": false,
        "fillLight": false,
        "exhaustFan": false,
        "alarm": false,
        "emergencyDoor": false,
        "dht11Status": true,
        "lightSensorStatus": true,
        "createTime": "2024-03-21T10:00:00"
    }
}
```

### 2. 更新设备控制
- **接口URL**: `/device/control`
- **请求方式**: PUT
- **请求参数**:
```json
{
    "warningLight": false,    // 警告灯
    "fillLight": false,       // 补光灯
    "exhaustFan": false,      // 排气扇
    "alarm": false,           // 报警器
    "emergencyDoor": false    // 紧急门
}
```
- **成功响应**:
```json
{
    "code": 200,
    "message": "设备控制成功",
    "data": {
        "warningLight": false,
        "fillLight": false,
        "exhaustFan": false,
        "alarm": false,
        "emergencyDoor": false
    }
}
```
- **失败响应**:
```json
{
    "code": 500,
    "message": "设备控制失败",
    "data": null
}
```

### 3. 获取设备控制记录
- **接口URL**: `/device/control/records`
- **请求方式**: GET
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
        {
            "id": 1,
            "userId": 1,
            "deviceStatusId": 1,
            "deviceType": 1,
            "controlAction": true,
            "createTime": "2024-03-21T10:00:00"
        }
    ]
}
```

### 4. 根据设备类型获取控制记录
- **接口URL**: `/device/control/records/{deviceType}`
- **请求方式**: GET
- **请求参数**: 
  - `deviceType`: 设备类型（1:警告灯, 2:补光灯, 3:排气扇, 4:报警器, 5:紧急门）
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
        {
            "id": 1,
            "userId": 1,
            "deviceStatusId": 1,
            "deviceType": 1,
            "controlAction": true,
            "createTime": "2024-03-21T10:00:00"
        }
    ]
}
```

## 阈值设置接口

### 1. 获取当前阈值设置
- **接口URL**: `/thresholds/current`
- **请求方式**: GET
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "id": 1,
        "temperatureMin": 20,
        "temperatureMax": 30,
        "humidityMin": 40,
        "humidityMax": 70,
        "lightMin": 300,
        "lightMax": 1000,
        "co2Min": 350,
        "co2Max": 1000,
        "createTime": "2024-03-21T10:00:00"
    }
}
```

### 2. 更新阈值设置
- **接口URL**: `/thresholds/update`
- **请求方式**: POST
- **请求参数**:
```json
{
    "temperatureMin": 20,    // 温度最小值
    "temperatureMax": 30,    // 温度最大值
    "humidityMin": 40,       // 湿度最小值
    "humidityMax": 70,       // 湿度最大值
    "lightMin": 300,         // 光照最小值
    "lightMax": 1000,        // 光照最大值
    "co2Min": 350,           // CO2最小值
    "co2Max": 1000           // CO2最大值
}
```
- **成功响应**:
```json
{
    "code": 200,
    "message": "更新成功",
    "data": null
}
```