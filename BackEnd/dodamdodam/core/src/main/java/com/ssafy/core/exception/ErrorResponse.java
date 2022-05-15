package com.ssafy.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private final String msg;
    private final int status;
    private final List<FieldError> errors;
    private final String code;
    private final LocalDateTime localDateTime;

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    )).collect(Collectors.toList());
        }
    }

    public ErrorResponse(String msg, int status, String code) {
        this.msg = msg;
        this.status = status;
        this.errors = new ArrayList<>();
        this.code = code;
        this.localDateTime = LocalDateTime.now();
    }

    public ErrorResponse(ErrorCode code, List<FieldError> of) {
        this.msg = code.getMessage();
        this.status = code.getStatus();
        this.errors = of;
        this.code = code.getCode();
        this.localDateTime = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.getMessage(), code.getStatus(), code.getCode());
    }

    public static ErrorResponse of(ErrorCode code, BindingResult e) {
        return new ErrorResponse(code, FieldError.of(e));
    }

    public static ErrorResponse of(CustomException customException) {
        if (customException.getDetailMessage() != null) {
            return new ErrorResponse(
                    customException.getDetailMessage(),
                    customException.getErrorCode().getStatus(),
                    customException.getErrorCode().getCode()
            );
        }

        return new ErrorResponse(
                customException.getErrorCode().getMessage(),
                customException.getErrorCode().getStatus(),
                customException.getErrorCode().getCode()
        );
    }
}
