package com.pioneer.aitest;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author pioneer
 * @since 11.0
 * Created on 2026/3/30
 */

@Slf4j
@SpringBootTest
class AgentHelperTest {

    @Resource
    AgentHelper agentHelper;

    @Test
    void chatWithText() {
        String userMessage = "你好，我是 pioneer";
        String res = agentHelper.chatWithText(userMessage);
        log.info(res);
    }

    @Test
    void chatWithMultiMessage() {
        UserMessage mulitMessage = UserMessage.from(
                TextContent.from("简洁幽默地描述一下这个图片的内容。"),
                ImageContent.from("http://xxxx/1.png")
        );
        String res = agentHelper.chatWithUserMessage(mulitMessage);
        log.info(res);
    }
}