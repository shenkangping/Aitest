package com.pioneer.aitest.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * @author pioneer
 * @since 11.0
 * Created on 2026/3/30
 */
//@AiService
public interface MyAgentAIService {
    @SystemMessage(fromResource = "my-sys-prompt.txt")
    String chat(@UserMessage String userMessage);

    @SystemMessage(fromResource = "my-sys-prompt.txt")
    Result<String> chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
