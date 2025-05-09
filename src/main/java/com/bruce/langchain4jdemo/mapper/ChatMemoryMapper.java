package com.bruce.langchain4jdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.langchain4jdemo.entity.ChatMemoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ChatMemoryMapper extends BaseMapper<ChatMemoryEntity> {
    ChatMemoryEntity findByMemoryId(@Param("memoryId") String memoryId);
}
