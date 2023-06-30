/**
 * @projectName mytrain
 * @package com.jiawa.train.common.config
 * @className com.jiawa.train.common.exception.GlobalExceptionHandler
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.common.exception;

/**
 * GlobalExceptionHandler
 *
 * @author MC
 * @description
 * @date 2023/6/21 13:29
 * @version 1.0
 */

import com.apxvt.common.response.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("异常: {}", e.getMessage());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        return Result.fail(HttpStatus.FORBIDDEN.value(), "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                              HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("异常: 不支持 {} 请求", e.getMethod());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        return Result.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Result<Object> handleServiceException(ServiceException e, HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("业务异常: {}", e.getMessage());
        logErrorWithSeparator();
        // log.error("异常堆栈: ", e); // 业务异常不打印堆栈信息

        Integer code = e.getCode();
        return code != null ? Result.fail(code, e.getMessage()) : Result.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("异常: 请求参数校验失败 - {}", e.getMessage());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e, HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("绑定异常: 请求参数绑定失败 - {}", e.getMessage());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        logErrorWithSeparator();
        String errorMessage = e.getMessage();
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "未知异常，操作失败";
        }
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("系统异常: {}", errorMessage);
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    private void logErrorWithSeparator() {
        log.error("—————————————————Exception——————————————————————");
    }

    // 可以定义更多的异常处理方法...
}

