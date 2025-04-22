package com.guhai.smartbuilding.interceptor;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;

    public TokenInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            log.info("令牌为空或格式错误，响应401状态码");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(401, "未授权")));
            return false;
        }

        token = token.substring(7);
        try {
            JwtUtils.parseJWT(token);
            //log.info("令牌合法，放行");
            return true;
        } catch (Exception e) {
            log.info("令牌非法，响应401状态码");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(401, "token无效或已过期")));
            return false;
        }
    }
} 