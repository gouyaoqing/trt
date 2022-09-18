package com.trt.common.data.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BusinessException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

}
