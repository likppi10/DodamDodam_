package com.ssafy.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;

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

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BindException e) {

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_REQUEST, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.INVALID_REQUEST.getStatus()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            DateTimeParseException.class,
            MissingPathVariableException.class})
    public ResponseEntity<ErrorResponse> handleCustomException() {

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.INVALID_REQUEST.getStatus()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERVAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.INTERVAL_SERVER_ERROR.getStatus()));
    }
}
