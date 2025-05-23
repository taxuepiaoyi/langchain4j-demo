package com.bruce.langchain4jdemo.service.impl;

import com.bruce.langchain4jdemo.aiservice.UserAiService;
import com.bruce.langchain4jdemo.dto.UserDTO;
import com.bruce.langchain4jdemo.service.UserService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserAiService userAiService;

    /**
     * 从文本中提取用户信息
     * @param userMassge
     * @return
     */
    @Override
    public UserDTO extractPersonFrom(String userMassge) {
        UserDTO userDTO = userAiService.extractPersonFrom(userMassge);
        return userDTO;
    }
}
