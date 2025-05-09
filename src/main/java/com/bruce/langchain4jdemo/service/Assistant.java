package com.bruce.langchain4jdemo.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface Assistant {
     String chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
