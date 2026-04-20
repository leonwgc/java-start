package com.example.jpa.common;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setCode(200);
        res.setMsg("success");
        res.setData(data);
        return res;
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setData(null);
        return res;
    }
}