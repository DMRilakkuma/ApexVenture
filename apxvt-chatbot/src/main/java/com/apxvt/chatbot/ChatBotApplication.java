package com.apxvt.chatbot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ComponentScan("com.apxvt")
@MapperScan("com.apxvt.chatbot.mapper")
@SpringBootApplication
public class ChatBotApplication {
	private static final Logger LOG = LoggerFactory.getLogger(ChatBotApplication.class);

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(ChatBotApplication.class);
		Environment env = app.run(args).getEnvironment();
		String IP = InetAddress.getLocalHost().getHostAddress();
		LOG.info("启动成功！！");
		LOG.info("运行环境: \t{}", env.getProperty("spring.profiles.active"));
		LOG.info("测试地址: \thttp://{}:{}{}", IP, env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
	}
}
