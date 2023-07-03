/**
 * @projectName ApexVenture
 * @package com.apxvt.auth.controller
 * @className com.apxvt.auth.controller.TokenController
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.auth.controller;

import com.apxvt.api.response.Result;
import com.apxvt.auth.form.LoginDto;
import com.apxvt.auth.service.SysLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TokenController
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/7/3 15:16
 */

@RestController
@Tag(name = "用户授权")
public class TokenController {

    @Resource
    private SysLoginService sysLoginService;

    @Operation(summary ="用户登录")
    @PostMapping("/login")
    public Result<String> login(@Validated LoginDto loginDto) {
        return sysLoginService.login(loginDto.getUsername(), loginDto.getPassword());
    }

}
 