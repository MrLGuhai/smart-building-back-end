package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password);
    User findByUsername(@Param("username") String username);
    int register(@Param("username") String username, @Param("password") String password);
} 