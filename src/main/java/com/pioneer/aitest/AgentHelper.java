package com.pioneer.aitest;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

/**
 * @author pioneer
 * @since 11.0
 * Created on 2026/3/30
 */

@Service
@Log4j
public class AgentHelper {

    @Resource
    ChatModel qwenChatModel;

    public String chat(String userMessage) {
        UserMessage question = UserMessage.from(userMessage);
        ChatResponse chat = qwenChatModel.chat(question);
        return chat.aiMessage().text();
    }

}
