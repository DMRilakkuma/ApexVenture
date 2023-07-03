/**
 * @projectName ApexVenture
 * @package com.apxvt.common.service.impl
 * @className com.apxvt.common.service.impl.StpInterfaceImpl
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.common.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.apxvt.common.mapper.UserAuthMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * StpInterfaceImpl
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/7/3 13:53
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserAuthMapper userAuthMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 数据库查询权限
        return userAuthMapper.selectAuthListByUserId((String) loginId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 当前用户的角色
        return userAuthMapper.selectPermissionListByUserId((String) loginId);
    }

}
 