package com.ssafy.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(MethodArgumentNotValidException e) {

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_REQUEST, e.getBindingResult());

        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.INVALID_REQUEST.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERVAL_SERVER_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.INTERVAL_SERVER_ERROR.getStatus()));
    }
}
