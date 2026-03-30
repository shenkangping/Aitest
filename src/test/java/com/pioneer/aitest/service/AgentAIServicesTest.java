package com.pioneer.aitest.service;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pioneer
 * @since 11.0
 * Created on 2026/3/30
 */
@SpringBootTest
@Slf4j
class AgentAIServicesTest {

    @Resource
    MyAgentAIService myAgentAIService ;
    @Test
    void chat() {
        String res = myAgentAIService.chat("介绍一下你自己");
        log.info(res);
    }

    @Test
    void chatWithMemory() {
        String res = myAgentAIService.chat("你好，我是pioneer");
        log.info(res);
        res = myAgentAIService.chat("我是谁，你记得我的名字吗");
        log.info(res);
    }


    @Test
    void testMemoryDirectly() {

        QwenChatModel qwenChatModel = QwenChatModel.builder().apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .modelName("qwen-plus").build();


        ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
        MyAgentAIService service = AiServices.builder(MyAgentAIService.class)
                .chatModel(qwenChatModel)
                .chatMemory(memory)
                .build();

        String res1 = service.chat("你好，我是pioneer");
        log.info(res1);
        String res2 = service.chat("我是谁，你记得我的名字吗");
        log.info(res2);  // 如果这里记得，说明是 Spring Bean 的问题
    }
}