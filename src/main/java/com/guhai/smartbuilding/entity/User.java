package com.guhai.smartbuilding.entity;

import lombok.Data;

@Data
public class User {
    private int id;              // 用户ID
    private String username;     // 用户名
    private String password;     // 密码（BCrypt加密后的密码，包含salt）
    // $[版本]$[成本]$[盐值22字符][哈希值31字符]
    /**
     * $2a$10$CXpXYvTPf.CVANIaigT0uun6..EtVtmcVTq2.ofn0Pn.EzitCgM8.
     * $是分割符，无意义；
     * 2a是bcrypt加密版本号；
     * 10是成本因子，默认为10，代表哈希计算进行了2^10=1024次迭代，数值越高越安全但同样的开销也越大；
     * 而后的前22位是salt值,salt是一个随机字符串，实际长度是16字节，使用Base64编码为22字符；
     * 再然后的字符串就是密码的密文了，也就是实际的密码哈希结果。
     */
    // 每次加密相同密码会得到不同结果，因为盐值是随机的
} 