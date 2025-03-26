package com.guhai.smartbuilding.common;

import lombok.Data;

@Data
public class ApiResponse {
    private Integer code;
    private String message;
    private Object data;

    public static ApiResponse success(Object data) {
        ApiResponse response = new ApiResponse();
        response.code = 200;
        response.message = "成功";
        response.data = data;
        return response;
    }

    public static ApiResponse success(String message, Object data) {
        ApiResponse response = new ApiResponse();
        response.code = 200;
        response.message = message;
        response.data = data;
        return response;
    }

    public static ApiResponse error(Integer code, String message) {
        ApiResponse response = new ApiResponse();
        response.code = code;
        response.message = message;
        return response;
    }
} 