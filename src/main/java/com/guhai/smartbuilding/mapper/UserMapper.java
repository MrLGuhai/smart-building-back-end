package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 根据用户名和密码查询用户
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    // 根据用户名查询用户
    User selectByUsername(@Param("username") String username);
    
    // 根据ID查询用户
    User selectById(@Param("id") int id);
    
    // 插入新用户
    int insert(User user);
} 