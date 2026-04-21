package com.example.jpa.exception;

import com.example.jpa.common.ApiResponse;
import com.example.jpa.common.ErrorCodeEnum;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 接口不存在
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<?> handle404(NoHandlerFoundException e) throws NoHandlerFoundException {
        String path = e.getRequestURL();
        if (path.contains("swagger") || path.contains("api-docs")) {
            throw e;
        }
        log.error("404 接口不存在: {}", path);
        return ApiResponse.fail(ErrorCodeEnum.NOT_FOUND);
    }

    // 404 静态资源不存在
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<?> handleResourceNotFound(NoResourceFoundException e) throws NoResourceFoundException {
        String path = e.getResourcePath();
        if (path.contains("swagger") || path.contains("api-docs") || path.contains("webjars")) {
            throw e;
        }
        log.error("404 静态资源不存在: {}", path);
        return ApiResponse.fail(ErrorCodeEnum.NOT_FOUND);
    }

    // 401 未授权
    @ExceptionHandler(SecurityException.class)
    public ApiResponse<?> handle401(SecurityException e) {
        log.error("未授权访问", e);
        return ApiResponse.fail(ErrorCodeEnum.UNAUTHORIZED);
    }

    // 处理 @Valid 校验异常（Spring Boot 3 通用）
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ApiResponse<?> handleValidException(Exception e) {
        String message;

        if (e instanceof MethodArgumentNotValidException ex) {
            // JSON 格式 @RequestBody 异常
            message = ex.getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("；"));
        } else if (e instanceof ConstraintViolationException ex) {
            // Spring Boot 3 / GET 参数 异常
            message = ex.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("；"));
        } else {
            message = "参数错误";
        }

        log.error("参数校验失败：{}", message);
        return ApiResponse.fail(ErrorCodeEnum.BAD_REQUEST.getCode(), message);
    }

    // 非法参数
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleBadRequest(IllegalArgumentException e) {
        log.error("参数错误：{}", e.getMessage());
        return ApiResponse.fail(ErrorCodeEnum.BAD_REQUEST);
    }

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusiness(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return ApiResponse.fail(e.getErrorCode());
    }

    // 系统兜底异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleServer(Exception e) {
        log.error("服务器未知异常", e);
        return ApiResponse.fail(ErrorCodeEnum.SERVER_ERROR);
    }
}