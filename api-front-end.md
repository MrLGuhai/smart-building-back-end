# 智能楼宇环境监控系统 API 接口文档

这是一个基于Spring Boot的后端项目。我的数据库连接方式是MySQL，数据库连接信息在application.yml中配置。

本文档详细描述了智能楼宇环境监控系统的部分后端API接口，包括接口名称、请求方法、请求参数、响应数据等信息。


## 基础说明

- 基础路径: `/api/v1`
- 响应格式: 所有接口统一返回格式
```json
{
  "code": 200,       // 状态码：200成功，非200失败
  "message": "xxx",  // 响应信息
  "data": {}        // 响应数据
}
```

## 用户登录相关接口

### 1. 用户登录

- **接口URL**: `/auth/login`
- **请求方法**: POST
- **请求参数**:
```json
{
  "user": {
    "username": "string",  // 用户名
    "password": "string",  // 密码
  }
}
```
- **响应数据**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "user": {
      "token": "string",      // JWT token
      "username": "string",   // 用户名
    }
  }
}
```

### 2. 用户注册

- **接口URL**: `/auth/register`
- **请求方法**: POST
- **请求参数**:
```json
{
  "user": {
    "username": "string",  // 用户名
    "password": "string"   // 密码
  }
}
```
- **响应数据**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "user": {
      "username": "string"  // 注册的用户名
    }
  }
}
```

### 3. 退出登录

- **接口URL**: `/auth/logout`
- **请求方法**: POST
- **请求参数**: 无
- **响应数据**:
```json
{
  "code": 200,
  "message": "退出成功",
  "data": null
}
```

## 环境数据相关接口

### 1. 获取实时环境数据

- **接口URL**: `/environment/current`
- **请求方法**: GET
- **请求参数**: 无
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "environment": {
      "temperature": number,    // 温度值
      "humidity": number,       // 湿度值
      "lightIntensity": number // 光照强度值
    }
  }
}
```

### 2. 获取设备状态

- **接口URL**: `/devices/status`
- **请求方法**: GET
- **请求参数**: 无
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "devices": {
      "status": {
        "warningLight": boolean,   // 警示灯状态
        "fillLight": boolean,      // 补光灯状态
        "exhaustFan": boolean,     // 排气扇状态
        "alarm": boolean,          // 警报器状态
        "emergencyDoor": boolean   // 应急逃生门状态
      },
      "sensors": {
        "dht11Status": "normal" | "fault",  // DHT11模块状态
        "lightSensorStatus": "normal" | "fault"  // 光敏模块状态
      }
    }
  }
}
```

## 阈值设置相关接口

### 1. 获取当前阈值设置

- **接口URL**: `/thresholds`
- **请求方法**: GET
- **请求参数**: 无
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "thresholds": {
      "temperature": number,     // 温度阈值
      "humidity": number,        // 湿度阈值
      "lightUpper": number,      // 光照强度上限
      "lightLower": number      // 光照强度下限
    }
  }
}
```

### 2. 更新阈值设置

- **接口URL**: `/thresholds`
- **请求方法**: PUT
- **请求参数**:
```json
{
  "thresholds": {
    "temperature": number,     // 温度阈值（可选）
    "humidity": number,        // 湿度阈值（可选）
    "lightUpper": number,     // 光照强度上限（可选）
    "lightLower": number     // 光照强度下限（可选）
  }
}
```
- **响应数据**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "thresholds": {
      "temperature": number,     // 更新后的温度阈值
      "humidity": number,        // 更新后的湿度阈值
      "lightUpper": number,     // 更新后的光照强度上限
      "lightLower": number     // 更新后的光照强度下限
    }
  }
}
```

## 设备控制相关接口

### 1. 获取设备控制状态

- **接口URL**: `/devices/control`
- **请求方法**: GET
- **请求参数**: 无
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "devices": {
      "warningLight": boolean,   // 警示灯状态
      "fillLight": boolean,      // 补光灯状态
      "exhaustFan": boolean,     // 排气扇状态
      "alarm": boolean,          // 警报器状态
      "emergencyDoor": boolean   // 应急逃生门状态
    }
  }
}
```

### 2. 更新设备控制状态

- **接口URL**: `/devices/control`
- **请求方法**: PUT
- **请求参数**:
```json
{
  "devices": {
    "warningLight": boolean,   // 警示灯状态
    "fillLight": boolean,      // 补光灯状态
    "exhaustFan": boolean,     // 排气扇状态
    "alarm": boolean,          // 警报器状态
    "emergencyDoor": boolean   // 应急逃生门状态
  }
}
```
- **响应数据**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "devices": {
      "warningLight": boolean,   // 更新后的警示灯状态
      "fillLight": boolean,      // 更新后的补光灯状态
      "exhaustFan": boolean,     // 更新后的排气扇状态
      "alarm": boolean,          // 更新后的警报器状态
      "emergencyDoor": boolean   // 更新后的应急逃生门状态
    }
  }
}
```

## 历史数据相关接口

### 1. 获取历史环境数据
- **接口URL**: `/environment/history`
- **请求方法**: GET
- **请求参数**: 
  - `limit`: number (可选，默认200) - 获取的记录数量
  - `startTime`: string (可选) - 开始时间，格式：yyyy-MM-dd HH:mm:ss
  - `endTime`: string (可选) - 结束时间，格式：yyyy-MM-dd HH:mm:ss
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
      {
        "id": 1,
        "temperature": 25.5,
        "humidity": 60,
        "light": 800,
        "createTime": "2024-03-21T10:00:00"
      }
    ]
  }
}
```

### 2. 获取历史阈值修改记录
- **接口URL**: `/threshold/records`
- **请求方法**: GET
- **请求参数**:
  - `limit`: number (可选，默认100) - 获取的记录数量
  - `thresholdType`: number (可选) - 阈值类型：1-温度，2-湿度，3-光照上限，4-光照下限
  - `userId`: number (可选) - 用户ID，用于查询特定用户的修改记录
  - `startTime`: string (可选) - 开始时间，格式：yyyy-MM-dd HH:mm:ss
  - `endTime`: string (可选) - 结束时间，格式：yyyy-MM-dd HH:mm:ss
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "thresholdType": 1,
        "oldValue": 30,
        "newValue": 35,
        "createTime": "2024-03-21T10:00:00"
      }
    ]
  }
}
```

### 3. 获取历史控制记录
- **接口URL**: `/device/control/records`
- **请求方法**: GET
- **请求参数**:
  - `limit`: number (可选，默认100) - 获取的记录数量
  - `deviceType`: number (可选) - 设备类型：1-警示灯，2-补光灯，3-排气扇，4-警报器，5-应急逃生门
  - `userId`: number (可选) - 用户ID，用于查询特定用户的控制记录
  - `startTime`: string (可选) - 开始时间，格式：yyyy-MM-dd HH:mm:ss
  - `endTime`: string (可选) - 结束时间，格式：yyyy-MM-dd HH:mm:ss
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
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
}
```

### 4. 获取历史告警记录
- **接口URL**: `/alarm/records`
- **请求方法**: GET
- **请求参数**:
  - `limit`: number (可选，默认100) - 获取的记录数量
  - `alarmType`: number (可选) - 告警类型：1-温度过高，2-温度过低，3-湿度过高，4-湿度过低，5-光照过强，6-光照过弱
  - `startTime`: string (可选) - 开始时间，格式：yyyy-MM-dd HH:mm:ss
  - `endTime`: string (可选) - 结束时间，格式：yyyy-MM-dd HH:mm:ss
- **响应数据**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
      {
        "id": 1,
        "alarmType": 1,
        "actualValue": 35,
        "thresholdValue": 30,
        "createTime": "2024-03-21T10:00:00"
      }
    ]
  }
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（未登录） |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 注意事项

1. 所有请求需要在 Header 中携带 token
```
Authorization: Bearer <token>
```

2. 所有数值类型的参数都需要进行合法性验证：
   - 温度范围：-50°C ~ 100°C
   - 湿度范围：0% ~ 100%
   - 光照强度范围：0 ~ 100000 lx

3. 当设置光照强度阈值时，如果同时设置上下限，需要确保上限大于下限

4. 设备控制状态的更新可能会有一定延迟，建议在前端做相应的提示

5. 建议在获取实时数据时使用轮询机制，推荐轮询间隔：5秒 