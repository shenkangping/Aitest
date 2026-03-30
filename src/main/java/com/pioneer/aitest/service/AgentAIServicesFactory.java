package com.pioneer.aitest.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pioneer
 * @since 11.0
 * Created on 2026/3/30
 */
@Configuration
public class AgentAIServicesFactory {
    @Resource
    ChatModel qwenChatModel;

    @Bean
    MyAgentAIService agentAIServices() {
        return AiServices.create(MyAgentAIService.class, qwenChatModel);
    }
}
