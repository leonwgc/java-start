package com.example.jpa.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    // 成功
    SUCCESS("0000", "成功"),

    // 客户端异常
    BAD_REQUEST("4000", "参数错误"),
    UNAUTHORIZED("4001", "未登录或权限不足"),
    NOT_FOUND("4004", "接口不存在"),

    // 业务异常
    DATA_NOT_EXIST("4100", "数据不存在"),
    DATA_EXIST("4101", "数据已存在"),

    // 服务器异常
    SERVER_ERROR("5000", "服务器内部错误"),
    NULL_POINTER("5001", "空指针异常");

    private final String code;
    private final String message;
}