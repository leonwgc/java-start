package com.example.jpa.exception;

import com.example.jpa.common.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Jackson序列化异常（你的LocalDateTime报错专属）
    @ExceptionHandler(InvalidDefinitionException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> jacksonError(InvalidDefinitionException e) {
        log.error("JSON序列化异常", e);
        return ApiResponse.error(500, "时间格式序列化错误，请检查LocalDateTime配置");
    }

    // 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> validError(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldErrors().get(0);
        String msg = error.getField() + ":" + error.getDefaultMessage();
        log.error("参数校验异常:{}", msg);
        return ApiResponse.error(400, msg);
    }

    // 日期解析异常
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> dateError(DateTimeParseException e) {
        log.error("日期格式错误", e);
        return ApiResponse.error(400, "日期格式请使用 yyyy-MM-dd HH:mm:ss");
    }

    // 404接口不存在
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> notFound(NoHandlerFoundException e) {
        log.error("404:{}", e.getRequestURL());
        return ApiResponse.error(404, "接口不存在");
    }

    // 通用运行异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> runtimeError(RuntimeException e) {
        log.error("业务异常", e);
        return ApiResponse.error(500, e.getMessage());
    }

    // 兜底所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> allError(Exception e) {
        log.error("系统异常", e);
        return ApiResponse.error(500, "服务器内部错误");
    }
}