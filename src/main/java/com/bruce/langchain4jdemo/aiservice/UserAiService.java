package com.bruce.langchain4jdemo.aiservice;

import com.bruce.langchain4jdemo.dto.UserDTO;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface UserAiService {
    // 提取用户信息
    @UserMessage("从{{userMassge}}中提取用户信息")
    UserDTO extractPersonFrom(@V("userMassge") String userMassge) ;
}
