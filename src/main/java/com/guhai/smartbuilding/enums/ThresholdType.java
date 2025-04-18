package com.guhai.smartbuilding.enums;

public enum ThresholdType {
    TEMPERATURE(1, "温度"),
    HUMIDITY(2, "湿度"),
    LIGHT_UPPER(3, "光照上限"),
    LIGHT_LOWER(4, "光照下限");

    private final int code;
    private final String description;

    ThresholdType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ThresholdType fromCode(int code) {
        for (ThresholdType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid threshold type code: " + code);
    }
} 