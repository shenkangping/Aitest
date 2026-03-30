package com.pioneer.aitest;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
    void chatWithUserMessage() {
        String userMessage = "你好，我是 pioneer";
        String res = agentHelper.chat(userMessage);
        log.info(res);
    }
}