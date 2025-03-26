package com.guhai.smartbuilding.interceptor;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行登录和注册接口
        if (request.getRequestURI().contains("/auth/login") || request.getRequestURI().contains("/auth/register")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(401, "未授权")));
            return false;
        }

        token = token.substring(7);
        if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(401, "token无效或已过期")));
            return false;
        }

        return true;
    }
} 