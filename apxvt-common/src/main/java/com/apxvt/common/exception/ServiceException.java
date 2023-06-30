/**
 * @projectName mytrain
 * @package com.jiawa.train.common.exception
 * @className com.jiawa.train.common.exception.ServiceException
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.common.exception;

import com.apxvt.common.response.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * ServiceException
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/6/21 13:37
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2653634707900442040L;

    private Integer code;

    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = Integer.parseInt(resultEnum.getCode());
        this.message = resultEnum.getMessage();
    }
}

 