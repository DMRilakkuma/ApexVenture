/**
 * @projectName ApexVenture
 * @package com.apxvt.common.mapper
 * @className com.apxvt.common.mapper.UserMapper
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper
 * @author MC
 * @description
 * @date 2023/7/3 14:33
 * @version 1.0
 */

@Mapper
public interface UserAuthMapper {
    List<String> selectPermissionListByUserId(@Param("loginId") String loginId);

    List<String> selectAuthListByUserId(@Param("loginId") String loginId);
}
 