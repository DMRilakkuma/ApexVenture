/**
 * @projectName ApexVenture
 * @package com.apxvt.auth.form
 * @className com.apxvt.auth.form.LoginDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.auth.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * LoginDto
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/7/3 15:29
 */

@Data
public class LoginDto {

    @Schema(description = "用户名")
    // @Length(min=4, max=8)
    private String username;

    @Schema(description = "密码")
    // @Length(min=8, max=16)
    private String password;

}
 