### 阈值分析接口

#### 获取阈值分析结果

- **接口URL**: `/thresholds/analysis`
- **请求方式**: GET
- **请求参数**:
  - `days`: 分析天数（可选，默认7天）
  - `thresholdType`: 阈值类型（可选，不传则分析所有类型）
    - 1: 温度
    - 2: 湿度
    - 3: 光照上限
    - 4: 光照下限

- **响应示例**:
```json
{
    "code": 200,
    "message": "分析成功",
    "data": [
        {
            "thresholdType": 1,
            "currentThreshold": 30,
            "suggestedThreshold": 28,
            "alarmFrequency": 2.5,
            "averageDeviation": 15.3,
            "suggestion": "告警频率较高，建议调整阈值。实际值与阈值偏差较大，建议重新评估阈值设置。告警主要集中在14:00时段，可考虑为该时段设置特殊阈值。",
            "timeDistribution": {
                "14:00": 5,
                "15:00": 3,
                "16:00": 2
            }
        }
    ]
}
```

- **响应字段说明**:
  - `thresholdType`: 阈值类型
  - `currentThreshold`: 当前阈值设置
  - `suggestedThreshold`: 建议的阈值设置
  - `alarmFrequency`: 平均每天告警次数
  - `averageDeviation`: 实际值与阈值的平均偏差
  - `suggestion`: 阈值调整建议
  - `timeDistribution`: 告警时间分布，key为小时，value为该时段的告警次数 