package com.tiv.im.study.gateway.exception;

import com.tiv.im.study.gateway.common.CodeEnum;
import com.tiv.im.study.gateway.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GlobalException.class)
    public Result<?> businessExceptionHandler(GlobalException e) {
        log.error("GlobalException", e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 运行时异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> businessExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return Result.error(CodeEnum.SYSTEM_ERROR);
    }

}
