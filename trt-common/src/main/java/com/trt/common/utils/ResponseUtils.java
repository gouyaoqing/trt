package com.trt.common.utils;

import com.trt.common.data.model.api.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseEntity<ResponseDTO<?>> success() {
        return new ResponseEntity<>(ResponseDTO.success(), HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDTO<? extends T>> success(T responseData) {
        return new ResponseEntity<>(ResponseDTO.success(responseData), HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDTO<?>> created(T responseData) {
        return new ResponseEntity<>(ResponseDTO.success(responseData), HttpStatus.OK);//HttpStatus.CREATED 应前端要求全要200
    }

    public static ResponseEntity<ResponseDTO<?>> moved(int count) {
        return new ResponseEntity<>(ResponseDTO.success(count), HttpStatus.OK);//HttpStatus.MOVED_PERMANENTLY
    }

    public static ResponseEntity<ResponseDTO<?>> internalError(String errorMsg) {
        return failed(HttpStatus.INTERNAL_SERVER_ERROR, ApiBusinessCode.INTERNAL_ERROR, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> resourceNotExist(String errorMsg) {
        return failed(HttpStatus.NOT_FOUND, ApiBusinessCode.TARGET_RESOURCES_NOT_EXIST, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> invalidParam(String errorMsg) {
        return failed(HttpStatus.BAD_REQUEST, ApiBusinessCode.PARAM_INVALID, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> businessError(HttpStatus httpStatus, String errorMsg) {
        return failed(httpStatus, ApiBusinessCode.BUSINESS_ERROR, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> dataError(HttpStatus httpStatus, String errorMsg) {
        return failed(httpStatus, ApiBusinessCode.DATA_ERROR, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> unauthorized(String errorMsg) {
        return failed(HttpStatus.UNAUTHORIZED, ApiBusinessCode.ACCESSED, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> forbidden(String errorMsg) {
        return failed(HttpStatus.FORBIDDEN, ApiBusinessCode.ACCESSED, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> timeout(String errorMsg) {
        return failed(HttpStatus.REQUEST_TIMEOUT, ApiBusinessCode.TIMEOUT, errorMsg);
    }

    public static ResponseEntity<ResponseDTO<?>> failed(HttpStatus httpStatus, ApiBusinessCode codeSupplier, String errorMsg) {
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header(ApiBusinessCode.CODE_HEADER, String.valueOf(codeSupplier.getCode()))
                .body(ResponseDTO.fault(httpStatus.value(), errorMsg));
    }

    public static ResponseEntity<ResponseDTO<?>> failed(ResponseDTO<?> responseData) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(ApiBusinessCode.CODE_HEADER, String.valueOf(ApiBusinessCode.INTERNAL_ERROR.getCode()))
                .body(ResponseDTO.fault(responseData.getErrorCode(), responseData.getErrorMsg()));
    }

    public static ResponseEntity<ResponseDTO<?>> failed() {
        return ResponseEntity.status(HttpStatus.OK)
                .header(ApiBusinessCode.CODE_HEADER, String.valueOf(ApiBusinessCode.INTERNAL_ERROR.getCode()))
                .body(ResponseDTO.fault(HttpStatus.INTERNAL_SERVER_ERROR.value(), "内部错误，请联系管理员"));
    }
}
