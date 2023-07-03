/**
 * @projectName ApexVenture
 * @package com.apxvt.auth.mapper
 * @className com.apxvt.auth.mapper.UserLoginMapper
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.auth.mapper;

import com.apxvt.api.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * UserLoginMapper
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/7/3 15:46
 */

@Mapper
public interface UserLoginMapper {

    SysUser selectUseByName(@Param("username") String username);
}