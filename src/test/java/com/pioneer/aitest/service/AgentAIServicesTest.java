package com.pioneer.aitest.service;

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
}