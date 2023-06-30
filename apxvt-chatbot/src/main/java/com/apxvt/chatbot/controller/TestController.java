package com.apxvt.chatbot.controller;


import com.apxvt.chatbot.mapper.UserMapper;
import com.apxvt.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("body")
@Tag(name = "body参数")
public class TestController {

    @Resource
    private UserMapper userMapper;

    @Operation(summary = "hello world")
    // @Parameter(name = "Authorization", description = "token", required = true, in = ParameterIn.HEADER)
    @GetMapping("/hello/id")
    public Result<String> hello(@Param("id") String id) {
        userMapper.selectUserId();
        return Result.ok("hello world" + id);
    }

}
