package com.example.caresystem.exception;

import com.example.caresystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：{}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler({BindException.class, ConstraintViolationException.class})
    public Result<Void> handleValidException(Exception e) {
        log.error("参数校验异常：{}", e.getMessage(), e);
        String msg = e.getMessage().split(":")[1].trim();
        return Result.error(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return Result.error("系统繁忙，请稍后重试");
    }
}