package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.web.dto.RestResponse;
import org.springframework.http.HttpStatus;

public class RestResponseBuilder {
    private static final String DEFAULT_SUCCESS_STATUS = "success";
    private static final String DEFAULT_FAILED_STATUS = "fail";

    public static RestResponse buildHttpResponse(HttpStatus status){
        return new RestResponse()
                .setStatus(status.getReasonPhrase())
                .setMessage(status.getReasonPhrase());
    }

    public static RestResponse buildSuccessResponse() {
        return new RestResponse()
                .setStatus(DEFAULT_SUCCESS_STATUS);
    }

    public static RestResponse buildSuccessResponse(String message) {
        return new RestResponse()
                .setStatus(DEFAULT_SUCCESS_STATUS)
                .setMessage(message);
    }

    public static <T> RestResponse<T> buildSuccessResponse(String message, T data) {
        return new RestResponse()
                .setStatus(DEFAULT_SUCCESS_STATUS)
                .setMessage(message)
                .setData(data);
    }

    public static <T> RestResponse<T> buildSuccessResponse(T data) {
        return new RestResponse()
                .setStatus(DEFAULT_SUCCESS_STATUS)
                .setData(data);
    }

    public static RestResponse buildFailedResponse() {
        return new RestResponse()
                .setStatus(DEFAULT_FAILED_STATUS);
    }

    public static RestResponse buildFailedResponse(String message) {
        return new RestResponse()
                .setStatus(DEFAULT_FAILED_STATUS)
                .setMessage(message);
    }

    public static <T> RestResponse<T> buildFailedResponse(String message, T data) {
        return new RestResponse()
                .setStatus(DEFAULT_FAILED_STATUS)
                .setMessage(message)
                .setData(data);
    }

    public static <T> RestResponse<T> buildFailedResponse(T data) {
        return new RestResponse()
                .setStatus(DEFAULT_FAILED_STATUS)
                .setData(data);
    }
    
}
