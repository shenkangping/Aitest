package com.pioneer.aitest.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
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

    /**
     * AIService的创建 基础使用
     * */
/*    @Bean
    MyAgentAIService agentAIServices() {
        return AiServices.create(MyAgentAIService.class, qwenChatModel);
    }*/


    /**
     * AIService的创建
     * 设置会话记忆 MessageWindowChatMemory
     * */
    @Bean
    MyAgentAIService agentAIServices() {
        ChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(MyAgentAIService.class)
                .chatModel(qwenChatModel)
                .chatMemory(messageWindowChatMemory)
                .build();
    }
}
