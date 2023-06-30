package com.apxvt.chatbot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@ComponentScan("com.pydance")
@MapperScan("com.pydance.chatbot.mapper")
@SpringBootApplication
public class ChatBotApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ChatBotApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ChatBotApplication.class);
       Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("运行环境: \t{}", env.getProperty("spring.profiles.active"));
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}
