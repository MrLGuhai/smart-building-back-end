package com.guhai.smartbuilding.mapper;

import com.guhai.smartbuilding.entity.Environment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnvironmentMapper {
    Environment getCurrentEnvironment();
    int insert(Environment environment);
} 