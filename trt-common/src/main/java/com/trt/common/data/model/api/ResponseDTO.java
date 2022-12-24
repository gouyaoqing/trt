package com.trt.common.data.model.api;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PROJECT: api-service
 * PACKAGE: com.jd.mlaas.apiservice.model.dto
 * DESC: Please input descriptions...
 *
 * @author huzhanfei
 * @since 2018/10/17
 */
@Data
@Accessors(chain = true)
public class ResponseDTO<T> {

    public static ResponseDTO<Void> success() {
        return new ResponseDTO<Void>().setSuccess(Boolean.TRUE);
    }

    public static <T> ResponseDTO<T> success(T responseData) {
        return new ResponseDTO<>(responseData).setSuccess(Boolean.TRUE);
    }

    public static <T> ResponseDTO<T> fault(int errorCode, String errorMsg) {
        return new ResponseDTO<T>(errorCode, errorMsg).setSuccess(Boolean.FALSE);
    }

    private ResponseDTO() {
    }

    private ResponseDTO(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private ResponseDTO(T responseData) {
        this.responseData = responseData;
    }

    /**
     * is_success : true
     * error_code : ""
     * error_msg : ""
     * error_details : {}
     * response_data : {}
     */

    @JSONField(name = "is_success")
    private Boolean success;
    @JSONField(name = "error_code")
    private Integer errorCode;
    @JSONField(name = "error_msg")
    private String errorMsg;
    @JSONField(name = "error_details")
    private Object errorDetails;
    @JSONField(name = "response_data")
    private T responseData;

}
