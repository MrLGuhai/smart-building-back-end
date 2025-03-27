package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByUsernameAndPassword(String username, String password);
    User selectByUsername(String username);
    int insert(User user);
} 