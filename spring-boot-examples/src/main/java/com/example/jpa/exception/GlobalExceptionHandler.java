package com.example.jpa.exception;

import com.example.jpa.common.ApiResponse;
import com.example.jpa.common.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<?> handle404() {
        return ApiResponse.fail(ErrorCodeEnum.NOT_FOUND);
    }

    // 401 未授权
    @ExceptionHandler(SecurityException.class)
    public ApiResponse<?> handle401() {
        return ApiResponse.fail(ErrorCodeEnum.UNAUTHORIZED);
    }

    // 400 参数错误
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleBadRequest() {
        return ApiResponse.fail(ErrorCodeEnum.BAD_REQUEST);
    }

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusiness(BusinessException e) {
        return ApiResponse.fail(e.getErrorCode());
    }

    // 空指针
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<?> handleNPE() {
        return ApiResponse.fail(ErrorCodeEnum.NULL_POINTER);
    }

    // 兜底
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleServer() {
        return ApiResponse.fail(ErrorCodeEnum.SERVER_ERROR);
    }
}