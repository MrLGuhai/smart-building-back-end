package com.guhai.smartbuilding.enums;

public enum DeviceType {
    WARNING_LIGHT(1, "警示灯"),
    FILL_LIGHT(2, "补光灯"),
    EXHAUST_FAN(3, "排气扇"),
    ALARM(4, "警报器"),
    EMERGENCY_DOOR(5, "应急逃生门");

    private final int code;
    private final String description;

    DeviceType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static DeviceType fromCode(int code) {
        for (DeviceType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid device type code: " + code);
    }
} 