/**
 * @projectName mytrain
 * @package com.jiawa.train.common.response
 * @className com.jiawa.train.common.response.Result
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Result
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/6/21 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7283104959571434994L;

    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "状态码")
    private String code;

    @Schema(description = "消息描述")
    private String message;

    @Schema(description = "数据")
    private T data;

    public Result(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.success = resultEnum == ResultEnum.OK;
    }

    public Result(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
        this.success = resultEnum == ResultEnum.OK;
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
        this.success = false;
    }

    public Result(int code, String message) {
        this.code = String.valueOf(code);
        this.message = message;
        this.success = false;
    }

    public Result(int code, String message, T data) {
        this.code = String.valueOf(code);
        this.message = message;
        this.data = data;
        this.success = false;
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultEnum.OK);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultEnum.OK, data);
    }

    public static <T> Result<List<T>> ok(List<T> data) {
        return new Result<>(ResultEnum.OK, data);
    }

    public static <T> Result<T> fail(String errorMessage) {
        for (ResultEnum resultEnum : ResultEnum.values()) {
            if (resultEnum.getMessage().equals(errorMessage)) {
                return new Result<>(resultEnum.getCode(), errorMessage);
            }
        }
        return new Result<>(ResultEnum.FAIL.getCode(), errorMessage);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message);
    }
}

