package com.example.jpa.exception;

import com.example.jpa.common.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}