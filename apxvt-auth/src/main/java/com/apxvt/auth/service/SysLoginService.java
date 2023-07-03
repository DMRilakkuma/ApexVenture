/**
 * @projectName ApexVenture
 * @package com.apxvt.auth.service
 * @className com.apxvt.auth.service.SysLoginService
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.auth.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.apxvt.api.domain.SysUser;
import com.apxvt.api.response.Result;
import com.apxvt.auth.mapper.UserLoginMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * SysLoginService
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/7/3 15:41
 */

@Service
public class SysLoginService {

    @Resource
    private UserLoginMapper userLoginMapper;

    /**
     * @param @param   username 用户名
     * @param password 密码
     * @return @return {@link String }
     * @author MC
     * @description 用户名密码登录
     * @date 2023/07/03  15:44:05
     **/
    public Result<String> login(String username, String password) {
        // 获取用户信息
        SysUser sysUser = userLoginMapper.selectUseByName(username);
        // 校验用户是否存在
        if (sysUser == null) {
            return Result.fail("用户不存在");
        }
        // 校验密码
        if (!sysUser.getPassword().equals(password)) {
            return Result.fail("密码错误");
        }
        // 登录
        StpUtil.login(sysUser.getUserId());
        // 返回token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return Result.success(tokenInfo.getTokenValue());
    }
}
 