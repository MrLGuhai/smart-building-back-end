package com.guhai.smartbuilding.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "Z3VoYWk=";    // 秘钥
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000;    // 12个小时

    /**
     * 生成JWT令牌
     * @param claims JWT令牌第二部分负载 payload 中要存储的内容
     * @return  生成的JWT令牌字符串
     */
    public static String generateJwt(Map<String,Object> claims){
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param token 要解析的JWT令牌
     * @return JWT令牌第二部分负载 payload 中存储的内容
     * @throws Exception 如果令牌无效或已过期，抛出异常
     */
    public static Claims parseJWT(String token) throws Exception{
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}