package com.example.jpa.common;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器：自动将 Controller 返回值包装为 ApiResponse
 *
 * 使用场景：
 * - Controller 方法直接返回业务对象（如 Product）
 * - 自动包装为统一格式：{"result":"success","timestamp":...,"data":{...}}
 *
 * 排除：
 * - 已经是 ApiResponse 类型的返回值不再重复包装
 * - String 类型需要特殊处理（防止 Jackson 序列化冲突）
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            @NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回值已经是 ApiResponse 类型，不需要包装
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(
            @Nullable Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {

        // 如果返回值已经是 ApiResponse，直接返回
        if (body instanceof ApiResponse) {
            return body;
        }

        // 自动包装为 ApiResponse
        return ApiResponse.success(body);
    }
}
