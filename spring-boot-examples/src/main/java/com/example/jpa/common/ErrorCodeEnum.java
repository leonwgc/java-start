package com.example.jpa.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    // 成功
    SUCCESS("200", "成功"),

    // 客户端异常
    BAD_REQUEST("400", "参数错误"),
    UNAUTHORIZED("401", "未登录或权限不足"),
    NOT_FOUND("404", "接口不存在"),

    // 业务异常
    DATA_NOT_EXIST("410", "数据不存在"),
    DATA_EXIST("411", "数据已存在"),

    // 服务器异常
    SERVER_ERROR("500", "服务器内部错误");

    private final String code;
    private final String message;
}