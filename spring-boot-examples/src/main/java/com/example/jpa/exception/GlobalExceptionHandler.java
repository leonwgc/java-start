package com.example.jpa.exception;

import com.example.jpa.common.ApiResponse;
import com.example.jpa.common.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - NoHandlerFoundException
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<?> handle404(NoHandlerFoundException e) {

        String path = e.getRequestURL();

        // 忽略 swagger 内部资源，不打印、不返回错误
        if (path.contains("swagger") || path.contains("api-docs")) {
            return null; // 直接跳过，不处理
        }

        log.error("404 接口不存在: {}", path);
        return ApiResponse.fail(ErrorCodeEnum.NOT_FOUND);
    }

    // 404 - NoResourceFoundException (静态资源不存在)
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<?> handleResourceNotFound(NoResourceFoundException e) {
        String path = e.getResourcePath();

        // 忽略 swagger 相关的静态资源404，避免日志污染
        if (path.contains("swagger") || path.contains("api-docs") || path.contains(".well-known")) {
            return null;
        }

        log.error("404 静态资源不存在: {}", path);
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