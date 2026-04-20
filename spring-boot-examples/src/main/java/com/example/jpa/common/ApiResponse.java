package com.example.jpa.common;

import lombok.Data;
import java.util.Date;

@Data
public class ApiResponse<T> {
    private String result;
    private long timestamp;
    private T data; // 成功时用
    private ErrorInfo error; // 失败时用

    // 成功返回
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setResult("success");
        res.setTimestamp(new Date().getTime());
        res.setData(data);
        return res;
    }

    // 失败返回
    public static ApiResponse<?> fail(String message, String code) {
        ApiResponse<?> res = new ApiResponse<>();
        res.setResult("fail");
        res.setTimestamp(new Date().getTime());

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage(message);
        errorInfo.setCode(code);
        res.setError(errorInfo);

        return res;
    }

    // 错误内部类
    @Data
    public static class ErrorInfo {
        private String message;
        private String code;
    }
}