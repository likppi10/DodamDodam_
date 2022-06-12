package com.ssafy.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String detailMessage;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

}
