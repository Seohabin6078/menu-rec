package com.yamukja.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    // TODO 스프링에서 제공하는 추상 클래스인 ResponseEntityExceptionHandler를 extends 하는 방법이나 추가적인 예외 처리는 이후에 더 추가하기!!
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse.FieldErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.FieldErrors.of(e.getBindingResult());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse.ConstraintViolations handleConstraintViolationException(ConstraintViolationException e) {
        return ErrorResponse.ConstraintViolations.of(e.getConstraintViolations());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse.Basic> handleBusinessLogicException(BusinessLogicException e) {
        ErrorResponse.Basic response = ErrorResponse.Basic.of(e.getExceptionCode());

        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse.Basic handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.Basic.of(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse.Basic handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ErrorResponse.Basic.of(HttpStatus.BAD_REQUEST, "Required request body is missing");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse.Basic handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ErrorResponse.Basic.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // db의 무결성을 위반한 경우(db 컬럼 제약 조건 위반)
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
//        return ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse.Basic handleException(Exception e) {
        log.error("# handle Exception", e);
        // TODO 애플리케이션의 에러는 에러 로그를 로그에 기록하고, 관리자에게 이메일, 카카오톡, 슬랙 등으로 알려주는 로직이 있는게 좋다.

        return ErrorResponse.Basic.of(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
