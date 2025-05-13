package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User selectByUsername(String username);
    
    // 根据ID查询用户
    User selectById(int id);
    
    // 插入新用户
    int insert(User user);
} 