package com.bruce.langchain4jdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_memory_entity")
public class ChatMemoryEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String memoryId;

    private String messagesJson;

    private LocalDateTime updatedAt;
}

