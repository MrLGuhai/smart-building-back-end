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

### RSA加密说明
1. 前端在调用登录/注册接口前，需要先调用`/auth/public-key`获取RSA公钥
2. 使用获取到的公钥对用户名和密码进行加密
3. 将加密后的数据发送到服务器
4. 服务器使用私钥解密后进行处理

## 认证相关接口

### 1. 获取RSA公钥
- **接口URL**: `/auth/public-key`
- **请求方式**: GET
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA..."  // RSA公钥
}
```

### 2. 用户登录
- **接口URL**: `/auth/login`
- **请求方式**: POST
- **请求参数**:
```json
{
    "username": "string",  // RSA加密后的用户名
    "password": "string"   // RSA加密后的密码
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

### 3. 用户注册
- **接口URL**: `/auth/register`
- **请求方式**: POST
- **请求参数**:
```json
{
    "username": "string",  // RSA加密后的用户名
    "password": "string"   // RSA加密后的密码
}
```
- **成功响应**:
```json
{
    "code": 200,
    "message": "注册成功",
    "data": null
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

### 4. 用户登出
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
        "smoke": 230,
        "createTime": "2024-03-21T10:00:00"
    }
}
```

### 2. 获取历史环境数据
- **接口URL**: `/environment/history`
- **请求方式**: GET
- **请求参数**:
  - `limit`: 记录数量（可选，默认200）
  - `startTime`: 开始时间（可选，格式：yyyy-MM-dd HH:mm:ss）
  - `endTime`: 结束时间（可选，格式：yyyy-MM-dd HH:mm:ss）
- **成功响应**:
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
                "smoke": 230,
                "createTime": "2024-03-21T10:00:00"
            }
        ]
    }
}
```
- **失败响应**:
```json
{
    "code": 500,
    "message": "获取历史环境数据失败",
    "data": null
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
### 3. 获取历史控制记录
- **接口URL**: `/device/control/records`
- **请求方式**: GET
- **请求参数**:
  - `limit`: 记录数量（可选，默认100）
  - `deviceType`: 设备类型（可选，1:警告灯, 2:补光灯, 3:排气扇, 4:报警器, 5:紧急门）
  - `userId`: 用户ID（可选）
  - `startTime`: 开始时间（可选，格式：yyyy-MM-dd HH:mm:ss）
  - `endTime`: 结束时间（可选，格式：yyyy-MM-dd HH:mm:ss）
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
- **失败响应**:
```json
{
    "code": 500,
    "message": "获取控制记录失败",
    "data": null
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

## 告警记录接口

### 1. 获取历史告警记录
- **接口URL**: `/alarm/records`
- **请求方式**: GET
- **请求参数**:
  - `limit`: 记录数量（可选，默认100）
  - `alarmType`: 告警类型（可选，1:温度过高, 2:温度过低, 3:湿度过高, 4:湿度过低, 5:光照过强, 6:光照过弱）
  - `startTime`: 开始时间（可选，格式：yyyy-MM-dd HH:mm:ss）
  - `endTime`: 结束时间（可选，格式：yyyy-MM-dd HH:mm:ss）
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "records": [
            {
                "id": 1,
                "alarmType": 1,
                "actualValue": 35.0,
                "thresholdValue": 30.0,
                "createTime": "2024-03-21T10:00:00"
            }
        ]
    }
}
```
- **失败响应**:
```json
{
    "code": 500,
    "message": "获取告警记录失败",
    "data": null
}
```

### 2. 获取未处理的告警记录
- **接口URL**: `/alarm/unprocessed`
- **请求方式**: GET
- **请求参数**: 无
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "records": [
            {
                "id": 1,
                "alarmType": 1,
                "actualValue": 35.0,
                "thresholdValue": 30.0,
                "createTime": "2024-03-21T10:00:00"
            }
        ]
    }
}
```
- **失败响应**:
```json
{
    "code": 500,
    "message": "获取未处理告警记录失败",
    "data": null
}
```
- **说明**:
  1. 该接口用于获取最新的未处理告警记录
  2. 每次调用后会清除已获取的告警记录，避免重复显示
  3. 建议前端每2秒调用一次该接口，以实时获取新的告警信息
  4. 告警类型说明：
     - 1: 温度过高
     - 2: 温度回归正常
     - 3: 湿度过高
     - 4: 湿度回归正常
     - 5: 光照过强
     - 6: 光照过弱

## 阈值修改记录接口

### 1. 获取历史阈值修改记录
- **接口URL**: `/thresholds/records`
- **请求方式**: GET
- **请求参数**:
  - `limit`: 记录数量（可选，默认100）
  - `thresholdType`: 阈值类型（可选，1:温度, 2:湿度, 3:光照上限, 4:光照下限）
  - `userId`: 用户ID（可选）
  - `startTime`: 开始时间（可选，格式：yyyy-MM-dd HH:mm:ss）
  - `endTime`: 结束时间（可选，格式：yyyy-MM-dd HH:mm:ss）
- **成功响应**:
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
- **失败响应**:
```json
{
    "code": 500,
    "message": "获取阈值修改记录失败",
    "data": null
}
```

### 2. 根据阈值类型获取修改记录
- **接口URL**: `/thresholds/records/type`
- **请求方式**: GET
- **请求参数**:
  - `userId`: 用户ID
  - `thresholdType`: 阈值类型（1:温度, 2:湿度, 3:光照上限, 4:光照下限）
- **成功响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
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

## 阈值分析接口

### 1. 获取阈值分析结果
- **接口URL**: `/thresholds/analysis`
- **请求方式**: GET
- **请求参数**:
  - `days`: 分析天数（可选，默认7天）
  - `thresholdType`: 阈值类型（可选，不传则分析所有类型）
    - 1: 温度
    - 2: 湿度
    - 3: 光照上限
    - 4: 光照下限
- **成功响应**:
```json
{
    "code": 200,
    "message": "分析成功",
    "data": [
        {
            "thresholdType": 1,                    // 阈值类型
            "currentThreshold": 30,                // 当前阈值设置
            "suggestedThreshold": 28,              // 建议的阈值设置
            "alarmFrequency": 2.5,                 // 平均每天告警次数
            "averageDeviation": 15.3,              // 实际值与阈值的平均偏差
            "suggestion": "告警频率较高，建议调整阈值;实际值与阈值偏差较大，建议重新评估阈值设置;告警主要集中在14:00时段，可考虑为该时段设置特殊阈值",  // 阈值调整建议，多条建议用分号分隔
            "timeDistribution": {                  // 告警时间分布
                "14:00": 5,                        // key为小时，value为该时段的告警次数
                "15:00": 3,
                "16:00": 2
            }
        }
    ]
}
```
- **失败响应**:
```json
{
    "code": 500,
    "message": "阈值分析失败",
    "data": null
}
```

### 建议说明
1. 告警频率建议：
   - 频率 > 3次/天：建议调整阈值
   - 频率 > 1次/天：可考虑微调阈值

2. 偏差建议：
   - 平均偏差 > 20：建议重新评估阈值设置

3. 时间分布建议：
   - 如果某个时段告警特别集中，会建议为该时段设置特殊阈值

4. 建议阈值计算：
   - 当告警频率 > 3次/天时，会根据实际值的分布调整阈值
   - 上限类型（温度、光照上限）：建议值 = 实际值平均值 + 5
   - 下限类型（湿度、光照下限）：建议值 = 实际值平均值 - 5