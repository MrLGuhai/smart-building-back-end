package com.guhai.smartbuilding.enums;

public enum AlarmType {
    TEMPERATURE_HIGH(1, "温度过高"),
    TEMPERATURE_LOW(2, "温度过低"),
    HUMIDITY_HIGH(3, "湿度过高"),
    HUMIDITY_LOW(4, "湿度过低"),
    LIGHT_HIGH(5, "光照过强"),
    LIGHT_LOW(6, "光照过弱");

    private final int code;
    private final String description;

    AlarmType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AlarmType fromCode(int code) {
        for (AlarmType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid alarm type code: " + code);
    }
} 