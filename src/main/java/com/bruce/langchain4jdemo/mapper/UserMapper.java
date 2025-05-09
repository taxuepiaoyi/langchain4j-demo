package com.bruce.langchain4jdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.langchain4jdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
