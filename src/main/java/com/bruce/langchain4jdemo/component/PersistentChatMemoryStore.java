package com.bruce.langchain4jdemo.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bruce.langchain4jdemo.entity.ChatMemoryEntity;
import com.bruce.langchain4jdemo.mapper.ChatMemoryMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
/**
 * 持久化ChatMemoryStore
 * @author ransong
 * @date 2023/10/19
 */
@Component
public class PersistentChatMemoryStore implements ChatMemoryStore {

    @Resource
    private ChatMemoryMapper chatMemoryMapper;

    @Override
    public List<ChatMessage> getMessages(Object obj) {
        if(obj == null ){
            throw new IllegalArgumentException("getMessages object cannot be null");
        }
        String memoryId = obj.toString();
        ChatMemoryEntity entity = chatMemoryMapper.findByMemoryId(memoryId) ;
        if( entity == null){
            return List.of();
        }
        return messagesFromJson(entity.getMessagesJson()) ;
    }

    @Override
    public void updateMessages(Object obj, List<ChatMessage> messages) {
        if(obj == null ){
            throw new IllegalArgumentException("getMessages object cannot be null");
        }
        String memoryId = obj.toString() ;
        String json = messagesToJson(messages);
        ChatMemoryEntity entity = chatMemoryMapper.findByMemoryId(memoryId);
        if (entity == null) {
            entity = new ChatMemoryEntity();
            entity.setMemoryId(memoryId);
        }
        entity.setMessagesJson(json);
        entity.setUpdatedAt(LocalDateTime.now());
        if (entity.getId() == null) {
            chatMemoryMapper.insert(entity);
        } else {
            chatMemoryMapper.updateById(entity);
        }
    }

    @Override
    public void deleteMessages(Object obj) {
        if(obj == null || !(obj instanceof String)){
            return  ;
        }
        String memoryId = (String) obj;
        LambdaQueryWrapper<ChatMemoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ChatMemoryEntity::getMemoryId, memoryId);
        chatMemoryMapper.delete(wrapper);
    }
}
